package com.kodat.of.halleyecommerce.mapper.category;

import com.kodat.of.halleyecommerce.category.Category;
import com.kodat.of.halleyecommerce.dto.category.MainCategoryDto;

public class MainCategoryMapper {

    public static MainCategoryDto toMainCategoryDto (Category category) {

        return MainCategoryDto.builder()
                .categoryId(category.getId())
                .categoryName(category.getCategoryName())
                .slug(category.getSlug())
                .imageUrl(category.getImageUrl())
                .build();
    }
}
