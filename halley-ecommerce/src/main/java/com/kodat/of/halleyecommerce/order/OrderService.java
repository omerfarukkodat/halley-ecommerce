package com.kodat.of.halleyecommerce.order;


import com.kodat.of.halleyecommerce.dto.order.OrderDto;
import com.kodat.of.halleyecommerce.order.enums.Status;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface OrderService {

    OrderDto createOrderFromCart(OrderDto orderDto, Authentication connectedUser);

    List<OrderDto> getAllOrders(Authentication connectedUser);

    OrderDto getOrderById(Long orderId, Authentication connectedUser);

    List<OrderDto> getAllOrdersForAdmin(Authentication connectedUser);

    OrderDto updateOrderStatus(Long orderId, Status status, Authentication connectedUser);

    List<OrderDto> getOrdersByDateRange(String startDate, String endDate, Authentication connectedUser);
}
