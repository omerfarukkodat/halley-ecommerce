package com.kodat.of.halleyecommerce.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
    private Long id;
    @NotNull(message = "Product cannot be null")
    private ProductResponseDto product;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
}
