package com.kodat.of.halleyecommerce.dto.cart;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDto  {
    private Long id;
    @NotEmpty(message = "Cart items cannot be empty")
    private List<CartItemDto> items = new ArrayList<>();


}
