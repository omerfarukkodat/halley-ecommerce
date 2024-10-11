package com.kodat.of.halleyecommerce.discount;


import com.kodat.of.halleyecommerce.dto.discount.DiscountDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface DiscountService {
    DiscountDto createDiscount(DiscountDto discountDto, Authentication connectedUser);

    void deleteDiscount(Long discountId, Authentication connectedUser);

    DiscountDto getDiscountById(Long discountId, Authentication connectedUser);

    List<DiscountDto> getAllDiscounts(Authentication connectedUser);
}
