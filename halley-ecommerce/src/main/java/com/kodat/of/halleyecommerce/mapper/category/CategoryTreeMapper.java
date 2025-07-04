package com.kodat.of.halleyecommerce.mapper.category;

import com.kodat.of.halleyecommerce.category.Category;
import com.kodat.of.halleyecommerce.dto.category.CategoryTreeDto;

import java.util.List;

public class CategoryTreeMapper {

    public static CategoryTreeDto toCategoryTreeDto(Category category) {
        return CategoryTreeDto.builder()
                .categoryId(category.getId())
                .categoryName(category.getCategoryName())
                .description(category.getDescription())
                .subCategories(toCategoryTreeDtoList(category.getSubCategories()))
                .slug(category.getSlug())
                .imageUrl(category.getImageUrl())
                .build();
    }


    public static List<CategoryTreeDto> toCategoryTreeDtoList(List<Category> categories) {
        return categories.stream()
                .map(CategoryTreeMapper::toCategoryTreeDto)
                .toList();
    }
}
