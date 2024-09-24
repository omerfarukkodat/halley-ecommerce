package com.kodat.of.halleyecommerce.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {
    private Long categoryId;
    @NotBlank(message = "Category name is mandatory")
    private String categoryName;

    private Long parentId; // Upper category id
}
