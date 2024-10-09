package com.kodat.of.halleyecommerce.validator;

import com.kodat.of.halleyecommerce.category.Category;
import com.kodat.of.halleyecommerce.category.CategoryRepository;
import com.kodat.of.halleyecommerce.exception.CategoryAlreadyExistsException;
import com.kodat.of.halleyecommerce.exception.CategoryDoesNotExistsException;
import com.kodat.of.halleyecommerce.exception.InvalidParentCategoryException;
import com.kodat.of.halleyecommerce.exception.ParentCategoryCycleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Set;

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
    public void validateCategoryIds(Set<Long> categoryIds) {
        for (Long categoryId: categoryIds){
            if (!categoryRepository.existsById(categoryId)){
                throw new CategoryDoesNotExistsException("Category: " + categoryId + " does not exists");
        }
        }
        LOGGER.info("Category id validation passed for: {}" , categoryIds);
    }

    public void validateParentCategory(Category category , Category parentCategory) {
        if (parentCategory != null && parentCategory.getParent() != null){
            throw new InvalidParentCategoryException("A category cannot be a child of another category that is already a child itself.");
        }
        if (category != null && isChildCategory(category,parentCategory)){
            throw new ParentCategoryCycleException("A category cannot be assigned as a parent of one of its own child categories, which would create a cycle.");
        }
    }

    private boolean isChildCategory(Category category, Category potentialParentCategory) {
        Category currentParentCategory = potentialParentCategory;
        while (currentParentCategory != null){
            if (currentParentCategory.equals(category)){
                return true;
            }
            currentParentCategory = currentParentCategory.getParent();
        }
        return false;
    }





}
