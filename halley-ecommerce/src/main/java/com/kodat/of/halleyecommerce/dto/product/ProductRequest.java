package com.kodat.of.halleyecommerce.dto.product;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {
    private Long id;
    private String productCode;
}
