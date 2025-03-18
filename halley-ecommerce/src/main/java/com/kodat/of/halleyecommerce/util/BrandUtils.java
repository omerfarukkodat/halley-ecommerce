package com.kodat.of.halleyecommerce.util;

import com.kodat.of.halleyecommerce.brand.Brand;
import com.kodat.of.halleyecommerce.brand.BrandRepository;
import com.kodat.of.halleyecommerce.exception.BrandAlreadyExists;
import com.kodat.of.halleyecommerce.exception.BrandNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BrandUtils {

    private final BrandRepository brandRepository;

    public Brand findBrand(String brandName) {
        return brandRepository.findBrandByName(brandName)
                .orElseThrow(() -> new BrandNotFoundException("Brand not found with name: " + brandName));
    }



    public void validateBrandId (Long brandId) {
         brandRepository.findById(brandId)
                .orElseThrow(() -> new BrandNotFoundException("Brand not found with id: " + brandId));
    }



    public void validateBrandName (String brandName) {
        if (brandRepository.findBrandByName(brandName)
                .isPresent()){
            throw new BrandAlreadyExists("Brand " + brandName + " already exists");
        }
    }

}
