package com.kodat.of.halleyecommerce.dto.product;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiscountRequest {
    @NotEmpty
    private List<Long> productIds;
    private BigDecimal discountPercentage;
}
