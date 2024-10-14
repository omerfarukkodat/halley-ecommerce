package com.kodat.of.halleyecommerce.dto.cart;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CartItemDto {
    private Long id;
    private ProductResponseDto product;
    private int quantity;
}
