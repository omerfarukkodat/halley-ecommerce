package com.kodat.of.halleyecommerce.dto.order;

import com.kodat.of.halleyecommerce.order.enums.Status;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
public class OrderDto {
    private Long id;
    private Long userId;
    @NotNull
    private List<OrderItemDto> orderItems;
    @NotNull
    private BigDecimal totalPrice;
    @NotNull
    private Status status;
    @NotNull
    private Long addressId;
}
