package com.kodat.of.halleyecommerce.dto.showcase;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ShowcaseDto {

    private Long id;

    private String imageUrl;
}
