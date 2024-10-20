package com.kodat.of.halleyecommerce.dto.cart;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {
    private Long id;
    @NotBlank(message = "Product name cannot be empty")
    private String name;

    @NotNull(message = "Original price cannot be null")
    private BigDecimal originalPrice;

    @NotNull(message = "Discounted price cannot be null")
    private BigDecimal discountedPrice;
    private String imageUrl;


}
