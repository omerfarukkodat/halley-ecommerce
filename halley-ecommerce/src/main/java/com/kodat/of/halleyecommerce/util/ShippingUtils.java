package com.kodat.of.halleyecommerce.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ShippingUtils {

    public BigDecimal calculateShippingCost(BigDecimal totalPrice){
        BigDecimal freeShippingThreshold = new BigDecimal("1000.00");
        BigDecimal shippingCost = new BigDecimal("150.00"); // Current shipping payment

        if (totalPrice.compareTo(freeShippingThreshold) > 0){
            return BigDecimal.ZERO;
        }
        else {
            return shippingCost;
        }
    }
}
