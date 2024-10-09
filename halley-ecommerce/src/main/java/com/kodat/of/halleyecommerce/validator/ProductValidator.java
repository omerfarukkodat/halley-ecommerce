package com.kodat.of.halleyecommerce.validator;

import com.kodat.of.halleyecommerce.exception.ProductAlreadyExistsException;
import com.kodat.of.halleyecommerce.exception.ProductNotFoundException;
import com.kodat.of.halleyecommerce.product.Product;
import com.kodat.of.halleyecommerce.product.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class ProductValidator {
    private final ProductRepository productRepository;


    public ProductValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validateProductCode(String productCode) {
        if (productRepository.existsByProductCode(productCode)) {
            throw new ProductAlreadyExistsException("Product with code: " + productCode + " already exists.");
        }
    }


    public void validateProductCode(String productCode , Long productId) {
        Product existingProduct = productRepository.findByProductCode(productCode);
        if (existingProduct != null && !existingProduct.getId().equals(productId)) {
            throw new ProductAlreadyExistsException("Product with code " + productCode + " already exists");
        }
    }

    public void validateProductId(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException("Product with id " + productId + " does not exist.");
        }
    }

    public Product validateProductAndFindById(Long productId) {
        validateProductId(productId);
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + productId + " does not exist1."));
    }


}
