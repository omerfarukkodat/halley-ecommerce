package com.kodat.of.halleyecommerce.dto.cart;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class ProductResponseDto {
    private Long id;
    private String name;
    private BigDecimal originalPrice;
    private BigDecimal discountedPrice;
    private String imageUrl;
}
