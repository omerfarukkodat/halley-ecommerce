package com.kodat.of.halleyecommerce.discount;

import com.kodat.of.halleyecommerce.exception.InvalidDiscountException;
import com.kodat.of.halleyecommerce.product.Product;
import com.kodat.of.halleyecommerce.product.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DiscountCalculator {
    private static final Logger LOGGER = LoggerFactory.getLogger(DiscountCalculator.class);



    private final ProductRepository productRepository;


    public DiscountCalculator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @Transactional
    public void applyDiscount(List<Long> productIds , BigDecimal discountPercentage , LocalDateTime startDate , LocalDateTime endDate) {

        BigDecimal discountMultiplier = calculateDiscountMultiplier(discountPercentage);
        List<Product> products = productRepository.findAllById(productIds);

        for (Product product : products) {
            // If discount is valid , calculated the discounted price and set
            if (isDiscountActive(startDate,endDate)){
                BigDecimal currentPrice = product.getOriginalPrice();
                BigDecimal discountedPrice = currentPrice.multiply(discountMultiplier);
                product.setDiscountedPrice(discountedPrice);
                LOGGER.info("Product ID: {} - Original Price: {}, Discounted Price: {}", product.getId(), currentPrice, discountedPrice);
            }else {
                product.setDiscountedPrice(product.getOriginalPrice());
                product.setDiscount(null);

            }
        }
        productRepository.saveAll(products);
    }

    private boolean isDiscountActive(LocalDateTime startDate, LocalDateTime endDate) {
        LocalDateTime now = LocalDateTime.now();
        boolean isActive = (startDate.isBefore(now) || startDate.isEqual(now)) && ( endDate.isAfter(now) || endDate.isEqual(now));
        LOGGER.debug("Is discount active? {}", isActive);
        return isActive;
    }

    public BigDecimal calculateDiscountMultiplier(BigDecimal discountPercentage) {
        if (discountPercentage.compareTo(BigDecimal.ZERO) < 0 || discountPercentage.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new InvalidDiscountException("Discount percentage must be between 0 and 100");
        }
        return BigDecimal.ONE.subtract(discountPercentage.divide(BigDecimal.valueOf(100)));
    }

}
