package com.kodat.of.halleyecommerce.discount;


import com.kodat.of.halleyecommerce.dto.discount.DiscountDto;
import com.kodat.of.halleyecommerce.mapper.discount.DiscountMapper;
import com.kodat.of.halleyecommerce.product.Product;
import com.kodat.of.halleyecommerce.product.ProductRepository;
import com.kodat.of.halleyecommerce.validator.DiscountValidator;
import com.kodat.of.halleyecommerce.validator.RoleValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscountServiceImpl implements DiscountService{
    private static final Logger LOGGER = LoggerFactory.getLogger(DiscountServiceImpl.class);



    private final DiscountRepository discountRepository;
    private final RoleValidator roleValidator;
    private final ProductRepository productRepository;
    private final DiscountCalculator discountCalculator;
    private final DiscountValidator discountValidator;

    public DiscountServiceImpl(DiscountRepository discountRepository, RoleValidator roleValidator, ProductRepository productRepository, DiscountCalculator discountCalculator, DiscountValidator discountValidator) {
        this.discountRepository = discountRepository;
        this.roleValidator = roleValidator;
        this.productRepository = productRepository;
        this.discountCalculator = discountCalculator;
        this.discountValidator = discountValidator;
    }


    @Override
    public DiscountDto createDiscount(DiscountDto discountDto, Authentication connectedUser) {
        roleValidator.verifyAdminRole(connectedUser);
        List<Product> products = productRepository.findAllById(discountDto.getProductIds());
        Discount discount = DiscountMapper.toDiscount(discountDto, products);
        Discount savedDiscount = discountRepository.save(discount);
        applyDiscountToProducts(savedDiscount);

        return DiscountMapper.toDiscountDto(savedDiscount);
    }

    @Override
    public void deleteDiscount(Long discountId, Authentication connectedUser) {
        roleValidator.verifyAdminRole(connectedUser);
        discountValidator.validateDiscount(discountId);
        LOGGER.info("Delete discount with id: {}", discountId);
        discountRepository.deleteById(discountId);
    }

    @Override
    public DiscountDto getDiscountById(Long discountId, Authentication connectedUser) {
        roleValidator.verifyAdminRole(connectedUser);
        discountValidator.validateDiscount(discountId);
        return DiscountMapper.toDiscountDto(discountRepository.findById(discountId).get());
    }


    private void applyDiscountToProducts(Discount discount){
        List<Long> productIds = discount.getProducts().stream()
                .map(Product::getId)
                .toList();
        discountCalculator.applyDiscount(productIds,discount.getDiscountPercentage(),discount.getStartDate(),discount.getEndDate());

    }


}
