package com.kodat.of.halleyecommerce.mapper.order;

import com.kodat.of.halleyecommerce.cart.Cart;
import com.kodat.of.halleyecommerce.cart.CartItem;
import com.kodat.of.halleyecommerce.dto.order.OrderDto;
import com.kodat.of.halleyecommerce.order.Order;
import com.kodat.of.halleyecommerce.order.OrderItem;
import com.kodat.of.halleyecommerce.user.User;

import java.math.BigDecimal;
import java.util.List;

public class OrderMapper {

    public static OrderItem toOrderItem(CartItem cartItem) {
        return OrderItem.builder()
                .product(cartItem.getProduct())
                .quantity(cartItem.getQuantity())
                .price(cartItem.getProduct().getDiscountedPrice())
                .totalPrice(cartItem.getProduct().getDiscountedPrice().multiply(new BigDecimal(cartItem.getQuantity())))
                .build();
    }
    public static List<OrderItem> toOrderItemList(List<CartItem> cartItems , Order order) {

        return cartItems.stream()
                .map(cartItem -> OrderItem.builder()
                        .product(cartItem.getProduct())
                        .quantity(cartItem.getQuantity())
                        .price(cartItem.getProduct().getDiscountedPrice())
                        .totalPrice(cartItem.getProduct().getDiscountedPrice().multiply(new BigDecimal(cartItem.getQuantity())))
                        .order(order)
                        .build())
                .toList();
    }
    public static OrderDto toOrderDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .userId(Long.valueOf(order.getUser().getId()))
                .orderItems(OrderItemMapper.toOrderItemDtoList(order.getOrderItems()))
                .totalPrice(order.getTotalPrice())
                .shippingCost(order.getShippingCost())
                .finalPrice(order.getFinalPrice())
                .status(order.getStatus())
                .addressId(order.getAddressId())
                .build();
    }
    public static OrderDto toOrderDtoForGuestUser(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .guestUserId(order.getGuestUser().getId())
                .orderItems(OrderItemMapper.toOrderItemDtoList(order.getOrderItems()))
                .totalPrice(order.getTotalPrice())
                .shippingCost(order.getShippingCost())
                .finalPrice(order.getFinalPrice())
                .status(order.getStatus())
                .addressId(order.getAddressId())
                .build();
    }

}
