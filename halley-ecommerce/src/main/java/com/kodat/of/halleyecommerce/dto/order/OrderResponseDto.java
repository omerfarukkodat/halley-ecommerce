package com.kodat.of.halleyecommerce.dto.order;

import com.kodat.of.halleyecommerce.order.enums.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
public class OrderResponseDto {
    private Long id;
    private List<OrderItemDto> orderItems;
    private BigDecimal totalPrice;
    private BigDecimal shippingCost;
    private BigDecimal finalPrice;
    private Status status;
    private String createdAt;
}
