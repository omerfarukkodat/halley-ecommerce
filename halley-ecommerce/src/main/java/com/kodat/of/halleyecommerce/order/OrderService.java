package com.kodat.of.halleyecommerce.order;


import com.kodat.of.halleyecommerce.dto.order.OrderDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface OrderService {

    OrderDto createOrderFromCart(OrderDto orderDto, Authentication connectedUser);

    List<OrderDto> getAllOrders(Authentication connectedUser);
}
