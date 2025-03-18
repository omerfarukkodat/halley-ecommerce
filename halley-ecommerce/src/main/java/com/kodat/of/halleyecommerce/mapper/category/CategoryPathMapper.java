package com.kodat.of.halleyecommerce.mapper.category;


import com.kodat.of.halleyecommerce.category.Category;
import com.kodat.of.halleyecommerce.dto.category.CategoryPathDto;


public class CategoryPathMapper {

    public static CategoryPathDto toCategoryPath(Category category) {
        return CategoryPathDto.builder()
                .id(category.getId())
                .name(category.getCategoryName())
                .description(category.getDescription())
                .slug(category.getSlug())
                .build();
    }

}
