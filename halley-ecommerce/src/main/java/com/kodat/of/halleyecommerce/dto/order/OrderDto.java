package com.kodat.of.halleyecommerce.dto.order;

import com.kodat.of.halleyecommerce.dto.cart.CartItemDto;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
public class OrderDto {
    private Long userId;
    @NotNull
    private List<CartItemDto> orderItems;
    @NotNull
    private BigDecimal totalPrice;
    @NotNull
    private String status;
    @NotNull
    private Long addressId;
}
