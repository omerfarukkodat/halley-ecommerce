package com.kodat.of.halleyecommerce.dto.category;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryTreeDto {
    private Long categoryId;
    private String categoryName;
    private List<CategoryTreeDto> subCategories;
}
