package com.kodat.of.halleyecommerce.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class EmailConsumerDto {

    private String firstName;
    private String lastName;
    private String email;
    private List<EmailOrderItemDto> orderItems;
    private BigDecimal shippingCost;
    private BigDecimal finalPrice;
}
