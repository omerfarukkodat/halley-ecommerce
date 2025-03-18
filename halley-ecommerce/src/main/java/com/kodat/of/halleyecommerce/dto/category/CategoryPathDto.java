package com.kodat.of.halleyecommerce.dto.category;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CategoryPathDto {

    private Long id;
    private String name;
    private String slug;
    private String description;

}
