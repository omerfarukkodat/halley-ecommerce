package com.kodat.of.halleyecommerce.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto implements Serializable {

    private Long id;

    @NotBlank(message = "Product name required")
    private String name;

    @NotBlank(message = "Product description required")
    private String description;

    private String brand;

    @NotNull(message = "Product price required")
    private BigDecimal originalPrice;

    private BigDecimal discountedPrice;

    @NotNull(message = "Product stock required")
    private Integer stock;

    private String size;

    private String wallpaperType;

    @NotBlank(message = "Product code is mandatory")
    private String productCode;

    private List<String> designName;

    private List<String> colourName;

    private List<String> roomName;

    private List<String> imageUrls;

    @NotNull(message = "At least one category Id is mandatory")
    private Set<Long> categoryIds;

    private String slug;

    private boolean isFeatured;
}
