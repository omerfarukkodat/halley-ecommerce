package com.kodat.of.halleyecommerce.product;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DiscountService {
    private final ProductRepository productRepository;


    public DiscountService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void applyDiscount(List<Long> productIds , BigDecimal discountPercentage) {
        BigDecimal discountMultiplier = calculateDiscountMultiplier(discountPercentage);
        List<Product> products = productRepository.findAllById(productIds);

        for (Product product : products) {
            BigDecimal currentPrice = product.getPrice();
            BigDecimal discountedPrice = currentPrice.multiply(discountMultiplier);
            product.setPrice(discountedPrice);
        }
        productRepository.saveAll(products);

    }

    public BigDecimal calculateDiscountMultiplier(BigDecimal discountPercentage) {
        return BigDecimal.ONE.subtract(discountPercentage.divide(BigDecimal.valueOf(100)));
    }
}
