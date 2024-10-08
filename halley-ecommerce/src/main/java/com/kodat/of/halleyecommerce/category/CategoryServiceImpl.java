package com.kodat.of.halleyecommerce.category;

import com.kodat.of.halleyecommerce.common.PageResponse;
import com.kodat.of.halleyecommerce.common.SlugService;
import com.kodat.of.halleyecommerce.dto.category.CategoryDto;
import com.kodat.of.halleyecommerce.dto.category.CategoryTreeDto;
import com.kodat.of.halleyecommerce.mapper.category.CategoryMapper;
import com.kodat.of.halleyecommerce.mapper.category.CategoryTreeMapper;
import com.kodat.of.halleyecommerce.util.CategoryUtils;
import com.kodat.of.halleyecommerce.validator.CategoryValidator;
import com.kodat.of.halleyecommerce.validator.RoleValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository categoryRepository;
    private final CategoryValidator categoryValidator;
    private final RoleValidator roleValidator;
    private final CategoryUtils categoryUtils;
    private final SlugService slugService;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryValidator categoryValidator, RoleValidator roleValidator, CategoryUtils categoryUtils, SlugService slugService) {
        this.categoryRepository = categoryRepository;
        this.categoryValidator = categoryValidator;
        this.roleValidator = roleValidator;
        this.categoryUtils = categoryUtils;
        this.slugService = slugService;
    }

    @Override
    public CategoryDto addParentCategory(CategoryDto categoryDto , Authentication connectedAdmin) {
        LOGGER.info("Adding parent category with name: {}", categoryDto.getCategoryName());
        roleValidator.verifyAdminRole(connectedAdmin); // Check admin role
        categoryValidator.validateCategoryName(categoryDto.getCategoryName()); // Check categoryName added before , null or empty?
        String slug = slugService.generateSlug(categoryDto.getCategoryName(),"");
        Category category = CategoryMapper.toCategory(categoryDto,null,slug);
        category.setSlug(slug);
        LOGGER.info("Parent category added successfully with ID: {}", category.getId());
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Transactional
    @Override
    public CategoryDto addChildCategory(Long parentCategoryId, CategoryDto categoryDto, Authentication connectedAdmin) {
        LOGGER.info("Adding child category with name: {}", categoryDto.getCategoryName());
       roleValidator.verifyAdminRole(connectedAdmin);
       categoryValidator.validateCategoryName(categoryDto.getCategoryName());
        Category parentCategory = categoryUtils.findCategoryById(parentCategoryId);
        String slug = slugService.generateSlug(categoryDto.getCategoryName(),""); // create slug
        Category childCategory = CategoryMapper.toCategory(categoryDto,parentCategory,slug);
        childCategory.setSlug(slug);
        categoryRepository.save(childCategory);
        LOGGER.info("Child category added successfully with name: {}", childCategory.getCategoryName());
        return CategoryMapper.toCategoryDto(childCategory);
    }

    @Override
    public PageResponse<CategoryDto> getAllCategories(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        List<CategoryDto> categoryDtoPage = categoryPage.
                stream().
                map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
        return new PageResponse<>(
                categoryDtoPage,
                categoryPage.getNumber(),
                categoryPage.getSize(),
                categoryPage.getTotalElements(),
                categoryPage.getTotalPages(),
                categoryPage.isFirst(),
                categoryPage.isLast()
        );
    }

    @Override
    public List<CategoryTreeDto> getCategoryTree() {
        List<Category> rootCategories = categoryRepository.findByParentIsNull();
        return rootCategories.stream()
                .map(CategoryTreeMapper::toCategoryTreeDto)
                .collect(Collectors.toList());
    }

}
