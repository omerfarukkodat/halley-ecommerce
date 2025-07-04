package com.kodat.of.halleyecommerce.order;

import com.kodat.of.halleyecommerce.dto.order.*;
import com.kodat.of.halleyecommerce.order.enums.Status;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
@Tag(name = "Orders")
@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrderFromCart(
            @Valid @RequestBody OrderDto orderDto,
            Authentication connectedUser
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrderFromCart(orderDto, connectedUser));
    }

    @GetMapping("/orderSummary/{orderId}")
    public ResponseEntity<OrderSummaryDto> getOrderSummary(
            @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderSummary(orderId));
    }


    @Secured("USER")
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAllOrders(Authentication connectedUser) {
        return ResponseEntity.ok(orderService.getAllOrders(connectedUser));
    }

    @Secured("USER")
    @GetMapping("/{orderId}")
    public ResponseEntity<UserOrderSummaryDto> getOrderById(
            @PathVariable Long orderId,
            Authentication connectedUser) {
        return ResponseEntity.ok(orderService.getOrderById(orderId, connectedUser));

    }

    @Secured("ADMIN")
    @GetMapping("/admin")
    public ResponseEntity<List<OrderResponseDto>> getAllOrdersForAdmin(Authentication connectedUser) {
        return ResponseEntity.ok(orderService.getAllOrdersForAdmin(connectedUser));
    }

    @Secured("ADMIN")
    @PutMapping("/{orderId}/status")
    public ResponseEntity<Void> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody UpdateOrderStatusDto orderStatusDto,
            Authentication connectedUser
    ) {
        Status status = orderStatusDto.getStatus();
        orderService.updateOrderStatus(orderId, status, connectedUser);
        return ResponseEntity.noContent().build();
    }

    @Secured("ADMIN")
    @GetMapping("/admin/filter")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate,
            Authentication connectedUser) {
        return ResponseEntity.ok(orderService.getOrdersByDateRange(startDate, endDate, connectedUser));
    }

    @Secured("ADMIN")
    @GetMapping("/admin/total-sales")
    public ResponseEntity<BigDecimal> getTotalSalesForAdmin(
            @RequestParam String startDate,
            @RequestParam String endDate,
            Authentication connectedUser) {
        return ResponseEntity.ok(orderService.getTotalSalesForAdmin(startDate, endDate, connectedUser));
    }
}
