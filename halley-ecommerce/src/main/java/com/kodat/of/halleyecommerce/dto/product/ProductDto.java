package com.kodat.of.halleyecommerce.dto.product;

import com.kodat.of.halleyecommerce.product.entity.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    @NotBlank(message = "Product name required")
    private String name;
    @NotBlank(message = "Product description required")
    private String description;
    @NotBlank(message = "Product price required")
    private BigDecimal price;
    @NotBlank(message = "Product stock required")
    private Integer stock;
    private String imageUrl;
    @NotBlank(message = "Category Id is mandatory")
    private Long categoryId;
}
