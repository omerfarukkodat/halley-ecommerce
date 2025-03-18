package com.kodat.of.halleyecommerce.mapper.product.attribute.colour;

import com.kodat.of.halleyecommerce.dto.product.attribute.colour.ColourDto;
import com.kodat.of.halleyecommerce.product.attribute.colour.Colour;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ColourMapper {

    public static Colour toColour(ColourDto colourDto) {

        return  Colour.builder()
                .name(colourDto.getName())
                .description(colourDto.getDescription())
                .imageUrl(colourDto.getImageUrl())
                .build();
    }

    public static ColourDto toColourDto(Colour colour) {

        return ColourDto.builder()
                .id(colour.getId())
                .name(colour.getName())
                .slug(colour.getSlug())
                .description(colour.getDescription())
                .imageUrl(colour.getImageUrl())
                .build();
    }

    public static List<ColourDto> toColourDtos(List<Colour> colours) {

        return colours.stream()
                .map(ColourMapper::toColourDto)
                .collect(Collectors.toList());

    }

}
