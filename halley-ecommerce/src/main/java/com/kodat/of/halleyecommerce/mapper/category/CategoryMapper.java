package com.kodat.of.halleyecommerce.mapper.category;

import com.kodat.of.halleyecommerce.category.Category;
import com.kodat.of.halleyecommerce.dto.category.CategoryDto;


public class CategoryMapper {

    public static CategoryDto toCategoryDto(Category category) {

        return CategoryDto.builder()
                .categoryId(category.getId())
                .categoryName(category.getCategoryName())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                .mainCategoryName(category.getParent() != null ? category.getParent().getCategoryName() : null)
                .slug(category.getSlug())
                .build();
    }

    public static Category toCategory(CategoryDto categoryDto , Category parentCategory ,String slug) {
        return Category.builder()
                .id(categoryDto.getCategoryId())
                .categoryName(categoryDto.getCategoryName())
                .parent(parentCategory)
                .slug(slug)
                .build();
    }

    public static Category updateCategoryFromDto(CategoryDto categoryDto, Category exsistingCategory , String slug , Category parentCategory) {
        exsistingCategory.setCategoryName(categoryDto.getCategoryName());
        exsistingCategory.setSlug(slug);
        if (parentCategory != null) {
            exsistingCategory.setParent(parentCategory);
        }
    return exsistingCategory;

    }

}
