package com.kodat.of.halleyecommerce.order;

import com.kodat.of.halleyecommerce.dto.order.OrderDto;
import com.kodat.of.halleyecommerce.dto.order.UpdateOrderStatusDto;
import com.kodat.of.halleyecommerce.order.enums.Status;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

   // @Secured("USER")
    @PostMapping
    public ResponseEntity<OrderDto> createOrderFromCart(
            @Valid @RequestBody OrderDto orderDto,
            Authentication connectedUser
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrderFromCart(orderDto, connectedUser));
    }

    @Secured("USER")
    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders(Authentication connectedUser) {
        return ResponseEntity.ok(orderService.getAllOrders(connectedUser));
    }

    @Secured("USER")
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderById(
            @PathVariable Long orderId,
            Authentication connectedUser) {
        return ResponseEntity.ok(orderService.getOrderById(orderId, connectedUser));

    }


    @Secured("ADMIN")
    @GetMapping("/admin")
    public ResponseEntity<List<OrderDto>> getAllOrdersForAdmin(Authentication connectedUser) {
        return ResponseEntity.ok(orderService.getAllOrdersForAdmin(connectedUser));
    }

    @Secured("ADMIN")
    @PutMapping("{orderId}/status")
    public ResponseEntity<OrderDto> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody UpdateOrderStatusDto orderStatusDto,
            Authentication connectedUser
    ){
        Status status = orderStatusDto.getStatus();
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId,status,connectedUser));
    }

    @Secured("ADMIN")
    @GetMapping("/admin/filter")
    public ResponseEntity<List<OrderDto>> getOrdersByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate,
            Authentication connectedUser) {
        return ResponseEntity.ok(orderService.getOrdersByDateRange(startDate,endDate,connectedUser));
    }

    @Secured("ADMIN")
    @GetMapping("/admin/total-sales")
    public ResponseEntity<BigDecimal> getTotalSalesForAdmin(
            @RequestParam String startDate,
            @RequestParam String endDate,
            Authentication connectedUser) {
        return ResponseEntity.ok(orderService.getTotalSalesForAdmin(startDate,endDate,connectedUser));
    }
}
