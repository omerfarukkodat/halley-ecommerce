package com.kodat.of.halleyecommerce.discount;


import com.kodat.of.halleyecommerce.dto.discount.DiscountDto;
import org.springframework.security.core.Authentication;

public interface DiscountService {
    DiscountDto createDiscount(DiscountDto discountDto, Authentication connectedUser);

    void deleteDiscount(Long discountId, Authentication connectedUser);
}
