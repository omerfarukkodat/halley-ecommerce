package com.kodat.of.halleyecommerce.dto.discount;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiscountDto {
    private Long id;
    @NotNull
    private BigDecimal discountPercentage;
    @NotNull
    private LocalDateTime startDate;
    @NotNull()
    private LocalDateTime endDate;
    @NotNull(message = "At least one product id is mandatory")
    private List<Long> productIds;

}
