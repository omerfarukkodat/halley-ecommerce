package com.kodat.of.halleyecommerce.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder

public class AddToCartRequest {
    @NotNull(message = "product id cannot be null")
    private Long productId;
    @Min(value = 1 , message = "quantity must be at least 1")
    private int quantity;


    
}
