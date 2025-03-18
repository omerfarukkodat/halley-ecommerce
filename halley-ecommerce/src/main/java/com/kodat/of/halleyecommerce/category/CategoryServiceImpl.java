package com.kodat.of.halleyecommerce.category;

import com.kodat.of.halleyecommerce.common.PageResponse;
import com.kodat.of.halleyecommerce.common.SlugService;
import com.kodat.of.halleyecommerce.dto.category.CategoryDto;
import com.kodat.of.halleyecommerce.dto.category.CategoryPathDto;
import com.kodat.of.halleyecommerce.dto.category.CategoryTreeDto;
import com.kodat.of.halleyecommerce.dto.category.MainCategoryDto;
import com.kodat.of.halleyecommerce.exception.CartNotFoundException;
import com.kodat.of.halleyecommerce.exception.CategoryDoesNotExistsException;
import com.kodat.of.halleyecommerce.exception.ParentCategoryDoesNotExistsException;
import com.kodat.of.halleyecommerce.mapper.category.CategoryMapper;
import com.kodat.of.halleyecommerce.mapper.category.CategoryPathMapper;
import com.kodat.of.halleyecommerce.mapper.category.CategoryTreeMapper;
import com.kodat.of.halleyecommerce.mapper.category.MainCategoryMapper;
import com.kodat.of.halleyecommerce.util.CategoryUtils;
import com.kodat.of.halleyecommerce.validator.CategoryValidator;
import com.kodat.of.halleyecommerce.validator.RoleValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository categoryRepository;
    private final CategoryValidator categoryValidator;
    private final RoleValidator roleValidator;
    private final CategoryUtils categoryUtils;
    private final SlugService slugService;


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
        categoryValidator.validateParentCategory(null,parentCategory);
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
                .collect(toList());
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
    @Cacheable(value = "categoryTree")
    @Override
    public List<CategoryTreeDto> getCategoryTree() {
        List<Category> rootCategories = categoryRepository.findByParentIsNull();
        return rootCategories.stream()
                .map(CategoryTreeMapper::toCategoryTreeDto)
                .collect(toList());
    }

    @Override
    public CategoryDto updateCategory(Long categoryId, CategoryDto categoryDto, Authentication connectedAdmin) {
        roleValidator.verifyAdminRole(connectedAdmin);
        categoryValidator.validateCategoryIds(Set.of(categoryId));
        categoryValidator.validateCategoryName(categoryDto.getCategoryName());
        Category existingCategory = categoryUtils.findCategoryById(categoryId);
        Category parentCategory = null;
        if (categoryDto.getParentId() != null){
             parentCategory = categoryUtils.findCategoryById(categoryDto.getParentId());
        }
        categoryValidator.validateParentCategory(existingCategory, parentCategory);
        String slug = slugService.generateSlug(categoryDto.getCategoryName(),"");
        Category updatedCategory = CategoryMapper.updateCategoryFromDto(categoryDto,existingCategory,slug,parentCategory);
        categoryRepository.save(updatedCategory);
        LOGGER.info("Updated category id : {} ,  name: {}",updatedCategory.getId(), updatedCategory.getCategoryName());
        return CategoryMapper.toCategoryDto(updatedCategory);
    }

    @Override
    public void deleteCategory(Long categoryId, Authentication connectedAdmin) {
        roleValidator.verifyAdminRole(connectedAdmin);
        categoryValidator.validateCategoryIds(Set.of(categoryId));
        LOGGER.info("Deleting category with id: {}", categoryId);
        categoryRepository.deleteById(categoryId);
    }

    @Cacheable(value = "mainCategories")
    @Override
    public List<MainCategoryDto> getMainCategories() {
        List<Category> mainCategories = categoryRepository.findByParentIsNull();
        if (!mainCategories.isEmpty()) {
            return mainCategories.stream()
                    .map(MainCategoryMapper::toMainCategoryDto)
                    .toList();
        }
        throw new ParentCategoryDoesNotExistsException("There are not any main categories");
    }

    @Override
    public CategoryTreeDto getCategory(String categorySlug) {

       Category category = categoryRepository.findCategoryBySlug(categorySlug)
               .orElseThrow(() -> new CategoryDoesNotExistsException("There is no category with id: " + categorySlug));
       return CategoryTreeMapper.toCategoryTreeDto(category);
    }

    @Override
    public List<CategoryPathDto> getCategoryPaths(String categorySlug) {

            Category category = categoryRepository.findCategoryBySlug(categorySlug)
                    .orElseThrow(() -> new CartNotFoundException("Category with id " + categorySlug + " not found"));

            List<CategoryPathDto> categoryPathDtos = new ArrayList<>();

        categoryPathDtos.add(CategoryPathMapper.toCategoryPath(category));

        if (category.getParent() == null){
             return categoryPathDtos;
            }

        categoryPathDtos.add(CategoryPathMapper.toCategoryPath(category.getParent()));
        Collections.reverse(categoryPathDtos);
        return categoryPathDtos;
    }

}
