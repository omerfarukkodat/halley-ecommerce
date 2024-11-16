package com.kodat.of.halleyecommerce.dto.order;


import com.kodat.of.halleyecommerce.dto.cart.ProductResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class EmailOrderItemDto {

    private ProductResponseDto product;
    private int quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;

}
