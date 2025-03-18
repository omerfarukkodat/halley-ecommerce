package com.kodat.of.halleyecommerce.brand;

import com.kodat.of.halleyecommerce.common.SlugService;
import com.kodat.of.halleyecommerce.dto.brand.BrandDto;
import com.kodat.of.halleyecommerce.exception.BrandNotFoundException;
import com.kodat.of.halleyecommerce.mapper.brand.BrandMapper;
import com.kodat.of.halleyecommerce.util.BrandUtils;
import com.kodat.of.halleyecommerce.validator.RoleValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final RoleValidator roleValidator;
    private final BrandUtils brandUtils;
    private final SlugService slugService;



    @Cacheable(value = "allBrand")
    @Override
    public List<BrandDto> getAll() {
        List<Brand> brands = brandRepository.findAll();
        if (brands.isEmpty()) {
            throw new BrandNotFoundException("Not found any Brands");
        }
        return brands.stream()
                .map(BrandMapper::toBrandDto)
                .collect(Collectors.toList());

    }


    @Override
    public void delete(Long id, Authentication connectedUser) {
        roleValidator.verifyAdminRole(connectedUser);
        brandUtils.validateBrandId(id);
        brandRepository.deleteById(id);
    }

    @Override
    public BrandDto add(BrandDto brandDto, Authentication connectedUser) {
        roleValidator.verifyAdminRole(connectedUser);
        brandUtils.validateBrandName(brandDto.getName());

        String brandSlug = slugService.generateSlugWithNoCode(brandDto.getName());
        Brand brand = BrandMapper.toBrand(brandDto,brandSlug);

        Brand savedBrand = brandRepository.save(brand);
        return BrandMapper.toBrandDto(savedBrand);
    }
}
