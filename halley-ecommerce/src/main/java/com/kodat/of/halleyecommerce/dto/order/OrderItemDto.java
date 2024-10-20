package com.kodat.of.halleyecommerce.dto.order;

import com.kodat.of.halleyecommerce.dto.cart.ProductResponseDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class OrderItemDto {
    @NotNull(message = "Product cannot be null")
    private ProductResponseDto product;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
}
