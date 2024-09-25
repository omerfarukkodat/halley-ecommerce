package com.kodat.of.halleyecommerce.validator;

import com.kodat.of.halleyecommerce.category.CategoryRepository;
import com.kodat.of.halleyecommerce.exception.CategoryAlreadyExistsException;
import com.kodat.of.halleyecommerce.exception.CategoryDoesNotExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CategoryValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryValidator.class);
    private final CategoryRepository categoryRepository;

    public CategoryValidator(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void validateCategoryName(String categoryName) {
        if (categoryRepository.existsByCategoryName(categoryName)){
            throw new CategoryAlreadyExistsException("Category: " + categoryName +" already exists");
        }
        LOGGER.info("Category name validation passed for: {}" , categoryName);
    }
    public void validateCategoryId(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)){
            throw new CategoryDoesNotExistsException("Category: " + categoryId + " does not exists");
        }
        LOGGER.info("Category id validation passed for: {}" , categoryId);
    }




}
