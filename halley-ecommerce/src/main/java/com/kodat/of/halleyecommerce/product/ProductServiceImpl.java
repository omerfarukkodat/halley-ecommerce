package com.kodat.of.halleyecommerce.product;

import com.kodat.of.halleyecommerce.dto.product.ProductDto;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Override
    public ProductDto addProduct(ProductDto productDto) {

    }
}
