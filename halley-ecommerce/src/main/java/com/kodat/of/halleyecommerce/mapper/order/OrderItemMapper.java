package com.kodat.of.halleyecommerce.mapper.order;


import com.kodat.of.halleyecommerce.dto.order.OrderItemDto;
import com.kodat.of.halleyecommerce.mapper.cart.CartMapper;
import com.kodat.of.halleyecommerce.order.OrderItem;

import java.util.List;

public class OrderItemMapper {
    public static OrderItemDto toOrderItemDto(OrderItem orderItem){
        return OrderItemDto.builder()
                .product(CartMapper.INSTANCE.toProductResponseDto(orderItem.getProduct()))
                .quantity(orderItem.getQuantity())
                .build();
    }


    public static List<OrderItemDto> toOrderItemDtoList(List<OrderItem> orderItems){
        return orderItems.stream()
                .map(OrderItemMapper::toOrderItemDto)
                .toList();
    }
}
