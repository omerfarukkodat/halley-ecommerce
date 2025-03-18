package com.kodat.of.halleyecommerce.mapper.order;

import com.kodat.of.halleyecommerce.address.Address;
import com.kodat.of.halleyecommerce.cart.CartItem;
import com.kodat.of.halleyecommerce.dto.order.OrderResponseDto;
import com.kodat.of.halleyecommerce.dto.order.OrderSummaryDto;
import com.kodat.of.halleyecommerce.dto.order.UserOrderSummaryDto;
import com.kodat.of.halleyecommerce.order.Order;
import com.kodat.of.halleyecommerce.order.OrderItem;
import com.kodat.of.halleyecommerce.order.enums.Status;
import com.kodat.of.halleyecommerce.user.GuestUser;
import com.kodat.of.halleyecommerce.user.User;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class OrderMapper {

    public static List<OrderItem> toOrderItemList(List<CartItem> cartItems, Order order) {

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

    public static OrderResponseDto toOrderResponseDto(Order order) {

        return OrderResponseDto.builder()
                .id(order.getId())
                .orderItems(OrderItemMapper.toOrderItemDtoList(order.getOrderItems()))
                .totalPrice(order.getTotalPrice())
                .shippingCost(order.getShippingCost())
                .finalPrice(order.getFinalPrice())
                .status(order.getStatus())
                .build();
    }

    public static Order createOrderEntityForNonMember(GuestUser guestUser, BigDecimal totalPrice, BigDecimal shippingCost, Long addressId) {
        return Order.builder()
                .guestUser(guestUser)
                .totalPrice(totalPrice)
                .shippingCost(shippingCost)
                .finalPrice(totalPrice.add(shippingCost))
                .addressId(addressId)
                .status(Status.HAZIRLANIYOR)
                .build();
    }

    public static Order createOrderForMemberUser(User user, BigDecimal totalPrice, BigDecimal shippingCost, Long addressId) {
        return Order.builder()
                .user(user)
                .totalPrice(totalPrice)
                .shippingCost(shippingCost)
                .finalPrice(totalPrice.add(shippingCost))
                .addressId(addressId)
                .status(Status.HAZIRLANIYOR)
                .build();
    }

    public static OrderSummaryDto toOrderSummaryDto(Order order) {
        return OrderSummaryDto.builder()
                .totalItems(order.getOrderItems().stream().map(OrderItem::getQuantity).reduce(0, Integer::sum))
                .shippingCost(order.getShippingCost())
                .totalPrice(order.getTotalPrice())
                .finalPrice(order.getFinalPrice())
                .build();
    }

    public static UserOrderSummaryDto toUserOrderSummaryDto(Order order) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM EEEE, HH:mm", Locale.forLanguageTag("tr"));
        return UserOrderSummaryDto.builder()
                .id(order.getId())
                .orderItems(OrderItemMapper.toOrderItemDtoList(order.getOrderItems()))
                .totalPrice(order.getTotalPrice())
                .shippingCost(order.getShippingCost())
                .finalPrice(order.getFinalPrice())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt().format(formatter))
                .build();
    }


}
