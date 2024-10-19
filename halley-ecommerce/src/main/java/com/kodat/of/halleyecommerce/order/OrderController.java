package com.kodat.of.halleyecommerce.order;

import com.kodat.of.halleyecommerce.dto.order.OrderDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
 @Secured("USER")
    @PostMapping
    public ResponseEntity<OrderDto> createOrderFromCart(
            @Valid @RequestBody OrderDto orderDto,
            Authentication connectedUser
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrderFromCart(orderDto,connectedUser));

    }
}
