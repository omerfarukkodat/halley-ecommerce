package com.kodat.of.halleyecommerce.order;


import com.kodat.of.halleyecommerce.dto.order.OrderDto;
import com.kodat.of.halleyecommerce.dto.order.OrderResponseDto;
import com.kodat.of.halleyecommerce.dto.order.OrderSummaryDto;
import com.kodat.of.halleyecommerce.dto.order.UserOrderSummaryDto;
import com.kodat.of.halleyecommerce.order.enums.Status;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {

    OrderResponseDto createOrderFromCart(OrderDto orderDto, Authentication connectedUser);

    List<OrderResponseDto> getAllOrders(Authentication connectedUser);

    UserOrderSummaryDto getOrderById(Long orderId, Authentication connectedUser);

    List<OrderResponseDto> getAllOrdersForAdmin(Authentication connectedUser);

    void updateOrderStatus(Long orderId, Status status, Authentication connectedUser);

    List<OrderResponseDto> getOrdersByDateRange(String startDate, String endDate, Authentication connectedUser);

    BigDecimal getTotalSalesForAdmin(String startDate, String endDate, Authentication connectedUser);

    OrderSummaryDto getOrderSummary(Long orderId);
}
