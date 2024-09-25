package com.kodat.of.halleyecommerce.product;

import com.kodat.of.halleyecommerce.dto.product.ProductDto;
import org.springframework.security.core.Authentication;

public interface ProductService {
    ProductDto addProduct(ProductDto productDto , Authentication connectedUser);

    ProductDto updateProduct(Long id, ProductDto productDto, Authentication connectedUser);

}
