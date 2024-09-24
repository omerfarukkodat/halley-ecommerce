package com.kodat.of.halleyecommerce.category;

import com.kodat.of.halleyecommerce.dto.category.CategoryDto;
import com.kodat.of.halleyecommerce.mapper.category.CategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Override
    public CategoryDto addParentCategory(CategoryDto categoryDto , Authentication connectedAdmin) {
        LOGGER.info("Adding parent category with name: {}", categoryDto.getCategoryName());

        verifyAdminRole(connectedAdmin); // Check admin role
        validateCategoryName(categoryDto.getCategoryName()); // Check categoryName added before?
        Category category = CategoryMapper.toCategory(categoryDto,null);
        LOGGER.info("Parent category added successfully with ID: {}", category.getId());

        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    private void verifyAdminRole(Authentication connectedAdmin) {
        if (connectedAdmin.getAuthorities().stream()
                .noneMatch(authority -> authority.getAuthority().equals("ADMIN"))){
            LOGGER.error("Unauthorized access attempt by user: {}", connectedAdmin.getName());
            throw new SecurityException("You are not an admin");
        }
        LOGGER.info("Admin role verified for user: {}", connectedAdmin.getName());

    }

    private void validateCategoryName(String categoryName) {
        if (categoryRepository.existsByCategoryName(categoryName)){
            LOGGER.warn("Attempt to add existing category: {}", categoryName);
            throw new RuntimeException("Category: "+ categoryName + " already exists");
        }
        LOGGER.info("Category name validation passed for: {}", categoryName);

    }


}
