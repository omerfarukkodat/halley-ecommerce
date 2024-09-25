package com.kodat.of.halleyecommerce.category;

import com.kodat.of.halleyecommerce.dto.category.CategoryDto;
import com.kodat.of.halleyecommerce.exception.CategoryAlreadyExistsException;
import com.kodat.of.halleyecommerce.exception.ParentCategoryDoesNotExistsException;
import com.kodat.of.halleyecommerce.exception.UnauthorizedAdminAccessException;
import com.kodat.of.halleyecommerce.mapper.category.CategoryMapper;
import com.kodat.of.halleyecommerce.validator.CategoryValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryServiceImpl implements CategoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository categoryRepository;
    private final CategoryValidator categoryValidator;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryValidator categoryValidator) {
        this.categoryRepository = categoryRepository;
        this.categoryValidator = categoryValidator;
    }

    @Override
    public CategoryDto addParentCategory(CategoryDto categoryDto , Authentication connectedAdmin) {
        LOGGER.info("Adding parent category with name: {}", categoryDto.getCategoryName());

        verifyAdminRole(connectedAdmin); // Check admin role
        categoryValidator.validateCategoryName(categoryDto.getCategoryName()); // Check categoryName added before , null or empty?
        Category category = CategoryMapper.toCategory(categoryDto,null);
        LOGGER.info("Parent category added successfully with ID: {}", category.getId());
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }
    @Transactional
    @Override
    public CategoryDto addChildCategory(String parentCategoryName, CategoryDto categoryDto, Authentication connectedAdmin) {
        LOGGER.info("Adding child category with name: {}", categoryDto.getCategoryName());
        verifyAdminRole(connectedAdmin);
       categoryValidator.validateCategoryName(categoryDto.getCategoryName());
        Category parentCategory = findParentCategory(parentCategoryName);
        Category childCategory = categoryRepository.save(CategoryMapper.toCategory(categoryDto,parentCategory));
        LOGGER.info("Child category added successfully with name: {}", childCategory.getCategoryName());
        return CategoryMapper.toCategoryDto(childCategory);
    }

    private void verifyAdminRole(Authentication connectedAdmin) {
        if (connectedAdmin.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ADMIN"))){
            throw new UnauthorizedAdminAccessException("Only the main admin can perform this operation");
        }
        LOGGER.info("Admin role verified for user: {}", connectedAdmin.getName());
    }
    
    private Category findParentCategory(String parentCategoryName) {
        return categoryRepository.findByCategoryName(parentCategoryName)
                .orElseThrow(() -> new ParentCategoryDoesNotExistsException("Parent category: " + parentCategoryName + " does not exist."));
    }



}
