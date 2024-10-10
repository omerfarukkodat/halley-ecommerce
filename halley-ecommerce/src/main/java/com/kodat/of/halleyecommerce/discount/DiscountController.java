package com.kodat.of.halleyecommerce.discount;

import com.kodat.of.halleyecommerce.dto.discount.DiscountDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/discounts")
public class DiscountController {
    private final DiscountService discountService;

    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }
    @Secured("ADMIN")
    @PostMapping
    public ResponseEntity<DiscountDto> createDiscount(
             @Valid @RequestBody DiscountDto discountDto,
             Authentication connectedUser
    ) {

        return ResponseEntity.status(HttpStatus.CREATED).body(discountService.createDiscount(discountDto,connectedUser));

    }



}
