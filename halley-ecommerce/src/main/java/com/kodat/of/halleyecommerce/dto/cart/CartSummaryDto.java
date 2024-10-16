package com.kodat.of.halleyecommerce.dto.cart;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
public class CartSummaryDto {
    private List<CartItemDto> cartItems;
    private BigDecimal totalPrice;
    private int totalItems;


}
