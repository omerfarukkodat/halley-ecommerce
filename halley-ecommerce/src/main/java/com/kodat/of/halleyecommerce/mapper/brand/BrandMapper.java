package com.kodat.of.halleyecommerce.mapper.brand;

import com.kodat.of.halleyecommerce.brand.Brand;
import com.kodat.of.halleyecommerce.dto.brand.BrandDto;

public class BrandMapper {



    public static Brand toBrand(BrandDto brandDto , String slug) {

        return Brand.builder()
                .name(brandDto.getName())
                .slug(slug)
                .imageUrl(brandDto.getImageUrl())
                .build();
    }

    public static BrandDto toBrandDto(Brand brand) {
        return BrandDto.builder()
                .id(brand.getId())
                .name(brand.getName())
                .slug(brand.getSlug())
                .imageUrl(brand.getImageUrl())
                .build();
    }



}
