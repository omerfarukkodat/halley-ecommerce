package com.kodat.of.halleyecommerce.validator;

import com.kodat.of.halleyecommerce.discount.DiscountRepository;
import com.kodat.of.halleyecommerce.exception.DiscountNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DiscountValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(DiscountValidator.class);

    private final DiscountRepository discountRepository;


    public DiscountValidator(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }


    public void validateDiscount(Long discountId) {

        if (!discountRepository.existsById(discountId)) {
            throw new DiscountNotFoundException("Discount with id " + discountId + " not found");
        }


    }

}
