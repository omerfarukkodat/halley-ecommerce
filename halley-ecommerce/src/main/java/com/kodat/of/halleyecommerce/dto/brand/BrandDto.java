package com.kodat.of.halleyecommerce.dto.brand;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandDto{

    private Long id;

    @NotBlank(message = "Brand name required")
    private String name;

    private String slug;

    @NotBlank(message = "Image url required")
    private String imageUrl;
}
