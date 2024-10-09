package com.kodat.of.halleyecommerce.product;

import com.kodat.of.halleyecommerce.common.PageResponse;
import com.kodat.of.halleyecommerce.dto.product.ProductDto;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.util.Set;

public interface ProductService {
    ProductDto addProduct(ProductDto productDto , Authentication connectedUser);

    ProductDto updateProduct(Long id, ProductDto productDto, Authentication connectedUser);

    PageResponse<ProductDto> findAllProducts(int page, int size,String sortBy , String sortDirection);

    ProductDto findProductById(Long productId);

    PageResponse<ProductDto> findProductsByCategoryId(int page, int size, Long categoryId , String sortBy , String sortDirection);

    void deleteProductById(Long productId , Authentication connectedUser);


    PageResponse<ProductDto> findProductsBySearch(String searchTerm,  int page, int size , String sortBy , String sortDirection);

    PageResponse<ProductDto> filterProducts(Set<Long> categoryIds, BigDecimal minPrice, BigDecimal maxPrice, int page, int size, String sortBy, String sortDirection);

    PageResponse<ProductDto> findSimilarProducts(Long productId, int page, int size);
}
