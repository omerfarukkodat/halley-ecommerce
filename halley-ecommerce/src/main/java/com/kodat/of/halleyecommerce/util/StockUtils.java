package com.kodat.of.halleyecommerce.util;

import com.kodat.of.halleyecommerce.exception.ProductNotFoundException;
import com.kodat.of.halleyecommerce.product.Product;
import com.kodat.of.halleyecommerce.product.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class StockUtils {
    private final ProductRepository productRepository;


    public StockUtils(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public boolean isStockAvailable(Long productId , int requestedQuantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        return product.getStock() >= requestedQuantity;
    }
    // Update Stock quantity after order completed
    public void updateStock(Long productId , int requiredStock) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        int updatedStock = product.getStock() - requiredStock;
        product.setStock(updatedStock);
        productRepository.save(product);
    }

    public boolean validateStockBeforeCheckout (Long productId , int quantityInCart){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        return product.getStock() >= quantityInCart;
    }








}
