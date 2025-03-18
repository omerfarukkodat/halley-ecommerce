package com.kodat.of.halleyecommerce.brand;

import com.kodat.of.halleyecommerce.dto.brand.BrandDto;
import org.springframework.security.core.Authentication;

import java.net.URI;
import java.util.List;

public interface BrandService {

    List<BrandDto> getAll();

    void delete(Long id, Authentication connectedUser);

    BrandDto add(BrandDto brandDto, Authentication connectedUser);
}
