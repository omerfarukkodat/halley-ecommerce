package com.kodat.of.halleyecommerce.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "Product price required")
    private BigDecimal price;
    @NotNull(message = "Product stock required")
    private Integer stock;
    @NotBlank(message = "Product code is mandatory")
    private String productCode;
    private String imageUrl;
    @NotNull(message = "Category Id is mandatory")
    private Long categoryId;
}