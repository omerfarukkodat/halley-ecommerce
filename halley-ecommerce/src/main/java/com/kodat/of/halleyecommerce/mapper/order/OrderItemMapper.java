package com.kodat.of.halleyecommerce.mapper.order;

import com.kodat.of.halleyecommerce.dto.cart.CartItemDto;
import com.kodat.of.halleyecommerce.mapper.cart.CartMapper;
import com.kodat.of.halleyecommerce.order.OrderItem;

import java.util.List;

public class OrderItemMapper {
    public static CartItemDto toCartItemDto(OrderItem orderItem){
        return CartItemDto.builder()
                .id(orderItem.getId())
                .product(CartMapper.INSTANCE.toProductResponseDto(orderItem.getProduct()))
                .quantity(orderItem.getQuantity())
                .build();
    }


    public static List<CartItemDto> toCartItemDtoList(List<OrderItem> orderItems){
        return orderItems.stream()
                .map(OrderItemMapper::toCartItemDto)
                .toList();
    }
}
