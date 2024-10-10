package com.kodat.of.halleyecommerce.dto.discount;

import com.kodat.of.halleyecommerce.dto.product.ProductRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiscountRequest {
    private Long id;
    @NotNull
    private BigDecimal discountPercentage;
    @NotNull
    private LocalDateTime startDate;
    @NotNull
    private LocalDateTime endDate;
    private List<ProductRequest> products;

}
