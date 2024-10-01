package com.kodat.of.halleyecommerce.product;

import com.kodat.of.halleyecommerce.common.PageResponse;
import com.kodat.of.halleyecommerce.dto.product.ProductDto;
import org.springframework.security.core.Authentication;

public interface ProductService {
    ProductDto addProduct(ProductDto productDto , Authentication connectedUser);

    ProductDto updateProduct(Long id, ProductDto productDto, Authentication connectedUser);

    PageResponse<ProductDto> findAllProducts(int page, int size);

    ProductDto findProductById(Long productId);

    PageResponse<ProductDto> findProductsByCategoryId(int page, int size, Long categoryId);

    void deleteProduct(Long productId , Authentication connectedUser);



}
