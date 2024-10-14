package com.kodat.of.halleyecommerce.dto.cart;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CartDto {
    private Long id;
    private List<CartItemDto> items;
}
