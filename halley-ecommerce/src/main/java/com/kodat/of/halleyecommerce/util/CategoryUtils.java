package com.kodat.of.halleyecommerce.util;

import com.kodat.of.halleyecommerce.category.Category;
import com.kodat.of.halleyecommerce.category.CategoryRepository;
import com.kodat.of.halleyecommerce.exception.CategoryDoesNotExistsException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class CategoryUtils {
    private final CategoryRepository categoryRepository;

    public CategoryUtils(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category findCategoryById(Long categoryId){
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryDoesNotExistsException("Category with ID: " + categoryId + " does not exist."));
    }

    public Long findCategoryBySlug(String slug){
        Category category = categoryRepository.findCategoryBySlug(slug)
                .orElseThrow(() -> new CategoryDoesNotExistsException("Category with name: " + slug + " does not exist."));

        return category.getId();
    }

    public Set<Long> findAllSubCategoryIds(Long categoryId) {
        Set<Long> subCategoryIds = new HashSet<>();
        List<Category> subCategories = categoryRepository.findByParent_Id(categoryId);

        for (Category subCategory : subCategories) {
            subCategoryIds.add(subCategory.getId());
            subCategoryIds.addAll(findAllSubCategoryIds(subCategory.getId()));
        }
        return subCategoryIds;
    }



}
