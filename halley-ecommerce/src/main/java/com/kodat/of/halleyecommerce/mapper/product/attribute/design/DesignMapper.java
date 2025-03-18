package com.kodat.of.halleyecommerce.mapper.product.attribute.design;

import com.kodat.of.halleyecommerce.dto.product.attribute.design.DesignDto;
import com.kodat.of.halleyecommerce.mapper.product.attribute.colour.ColourMapper;
import com.kodat.of.halleyecommerce.product.attribute.design.Design;

import java.util.List;
import java.util.stream.Collectors;

public class DesignMapper {

    public static Design toDesign(DesignDto designDto) {

        return  Design.builder()
                .name(designDto.getName())
                .description(designDto.getDescription())
                .imageUrl(designDto.getImageUrl())
                .build();
    }

    public static DesignDto toDesignDto(Design design) {

        return DesignDto.builder()
                .id(design.getId())
                .name(design.getName())
                .slug(design.getSlug())
                .description(design.getDescription())
                .imageUrl(design.getImageUrl())
                .build();
    }

    public static List<DesignDto> toDesignDtoList(List<Design> designs) {

        return designs.stream()
                .map(DesignMapper::toDesignDto)
                .collect(Collectors.toList());

    }
}
