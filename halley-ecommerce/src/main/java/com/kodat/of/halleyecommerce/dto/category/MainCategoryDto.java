package com.kodat.of.halleyecommerce.dto.category;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MainCategoryDto {
        private Long categoryId;
        private String categoryName;
        private String slug;
        private String imageUrl;

}
