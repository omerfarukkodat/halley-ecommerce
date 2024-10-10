package com.kodat.of.halleyecommerce.discount;

import com.kodat.of.halleyecommerce.product.Product;
import com.kodat.of.halleyecommerce.product.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DiscountCalculator {
    private final ProductRepository productRepository;


    public DiscountCalculator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void applyDiscount(List<Long> productIds , BigDecimal discountPercentage , LocalDateTime startDate , LocalDateTime endDate) {

        BigDecimal discountMultiplier = calculateDiscountMultiplier(discountPercentage);
        List<Product> products = productRepository.findAllById(productIds);

        for (Product product : products) {
            // If discount is valid , save the original price and calculate new price
            if (isDiscountActive(startDate,endDate)){
                BigDecimal currentPrice = product.getOriginalPrice();
                BigDecimal discountedPrice = currentPrice.multiply(discountMultiplier);
                product.setDiscountedPrice(discountedPrice);
            }
        }
        productRepository.saveAll(products);

    }

    private boolean isDiscountActive(LocalDateTime startDate, LocalDateTime endDate) {
        LocalDateTime now = LocalDateTime.now();
        return (startDate.isBefore(now) && startDate.isEqual(now)) && ( endDate.isAfter(now) || endDate.isEqual(now));
    }

    public BigDecimal calculateDiscountMultiplier(BigDecimal discountPercentage) {
        if (discountPercentage.compareTo(BigDecimal.ZERO) < 0 || discountPercentage.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
        }
        return BigDecimal.ONE.subtract(discountPercentage.divide(BigDecimal.valueOf(100)));
    }
}
