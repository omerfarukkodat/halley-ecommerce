package com.kodat.of.halleyecommerce.dto.order;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class OrderSummaryDto {
    private BigDecimal totalPrice;
    private BigDecimal shippingCost;
    private BigDecimal finalPrice;
    private int totalItems;
}
