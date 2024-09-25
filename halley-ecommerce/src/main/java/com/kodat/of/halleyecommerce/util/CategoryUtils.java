package com.kodat.of.halleyecommerce.util;

import com.kodat.of.halleyecommerce.category.Category;
import com.kodat.of.halleyecommerce.category.CategoryRepository;
import com.kodat.of.halleyecommerce.exception.ParentCategoryDoesNotExistsException;
import org.springframework.stereotype.Component;

@Component
public class CategoryUtils {
    private final CategoryRepository categoryRepository;

    public CategoryUtils(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category findParentCategory(String parentCategoryName) {
        return categoryRepository.findByCategoryName(parentCategoryName)
                .orElseThrow(() -> new ParentCategoryDoesNotExistsException("Parent category: " + parentCategoryName + " does not exist."));
    }
    public Category findCategoryById(Long categoryId){
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryDoesNotExistsException("Category with ID: " + categoryId + " does not exist."));
    }


}
