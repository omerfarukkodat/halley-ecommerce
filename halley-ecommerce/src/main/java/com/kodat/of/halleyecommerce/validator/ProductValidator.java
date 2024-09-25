package com.kodat.of.halleyecommerce.validator;

import com.kodat.of.halleyecommerce.product.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class ProductValidator {
    private final ProductRepository productRepository;


    public ProductValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public void validateProductCode(String productCode) {
        if (productRepository.existsByProductCode(productCode)){
            throw new ProductAlreadyExistsException("Product with code: " + productCode + " already exist.");
        }
    }


}
