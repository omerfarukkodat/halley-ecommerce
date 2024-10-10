package com.kodat.of.halleyecommerce.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private Long id;
    @NotBlank(message = "Product name required")
    private String name;
    @NotBlank(message = "Product description required")
    private String description;
    @NotNull(message = "Product price required")
    private BigDecimal originalPrice;
    private BigDecimal discountedPrice;
    @NotNull(message = "Product stock required")
    private Integer stock;
    @NotBlank(message = "Product code is mandatory")
    private String productCode;
    private String imageUrl;
    @NotNull(message = "At least one category Id is mandatory")
    private Set<Long> categoryIds;
    private String slug;
    private boolean isFeatured;
}
