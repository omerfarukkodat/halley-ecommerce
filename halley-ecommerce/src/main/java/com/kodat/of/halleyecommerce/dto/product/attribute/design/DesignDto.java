package com.kodat.of.halleyecommerce.dto.product.attribute.design;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DesignDto {

    private Long id;
    private String name;
    private String slug;
    private String description;
    private String imageUrl;
}
