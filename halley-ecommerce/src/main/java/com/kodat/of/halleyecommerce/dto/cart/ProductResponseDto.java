package com.kodat.of.halleyecommerce.dto.cart;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto  {
    private Long id;
    @NotBlank(message = "Product name cannot be empty")
    private String name;

    @NotNull(message = "Original price cannot be null")
    private BigDecimal originalPrice;

    @NotNull(message = "Discounted price cannot be null")
    private BigDecimal discountedPrice;
    private List<String> imageUrls;

    private String productCode;
    private String slug;
    private Integer stock;




}
