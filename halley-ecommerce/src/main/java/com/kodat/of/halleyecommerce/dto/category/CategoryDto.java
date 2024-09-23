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
    @NotNull(message = "Category id is mandatory")
    private Long categoryId;
    @NotBlank(message = "Category name is mandatory")
    private String categoryName;
}
