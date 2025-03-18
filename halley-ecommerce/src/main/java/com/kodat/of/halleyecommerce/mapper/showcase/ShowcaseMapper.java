package com.kodat.of.halleyecommerce.mapper.showcase;

import com.kodat.of.halleyecommerce.dto.showcase.ShowcaseDto;
import com.kodat.of.halleyecommerce.showcase.Showcase;

public class ShowcaseMapper {

    public static Showcase toShowcase(ShowcaseDto showcaseDto) {

       return Showcase.builder()
                .imageUrl(showcaseDto.getImageUrl())
                .build();
    }

    public static ShowcaseDto toShowcaseDto(Showcase showcase) {

        return ShowcaseDto.builder()
                .id(showcase.getId())
                .imageUrl(showcase.getImageUrl())
                .build();
    }




}
