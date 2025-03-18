package com.kodat.of.halleyecommerce.product;

import com.kodat.of.halleyecommerce.common.PageResponse;
import com.kodat.of.halleyecommerce.dto.category.CategoryPathDto;
import com.kodat.of.halleyecommerce.dto.product.ProductDto;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    ProductDto addProduct(ProductDto productDto, Authentication connectedUser);

    ProductDto updateProduct(Long id, ProductDto productDto, Authentication connectedUser);

    ProductDto findProductById(Long productId);

    void deleteProductById(Long productId, Authentication connectedUser);

    PageResponse<ProductDto> findProductsByCategoryId(
            int page,
            int size,
            String slug,
            String sortBy,
            String sortDirection,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Long brand,
            String wallpaperType,
            String wallpaperSize
    );

    PageResponse<ProductDto> findSimilarProducts(Long productId, int page, int size);


    Integer getStockById(Long productId);

    List<CategoryPathDto> getCategoryPaths(Long productId, Long categoryId);

    PageResponse<ProductDto> findAllProducts(
            int page,
            int size,
            String sortBy,
            String sortDirection,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Long categoryId,
            Long brand,
            String wallpaperType,
            String wallpaperSize);

    PageResponse<ProductDto> findProductsBySearch(
            String searchTerm,
            int page,
            int size,
            String sortBy,
            String sortDirection,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Long categoryId,
            Long brand,
            String wallpaperType,
            String wallpaperSize);

    PageResponse<ProductDto> filterProducts(
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            int page,
            int size,
            String sortBy,
            String sortDirection,
            Long brand,
            String wallpaperType,
            String wallpaperSize);

    PageResponse<ProductDto> findAllByBrand(
            String brand,
            int page,
            int size,
            String sortBy,
            String sortDirection,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Long categoryId,
            String wallpaperType,
            String wallpaperSize);

    PageResponse<ProductDto> getDiscountedProducts(
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            int page,
            int size,
            String sortBy,
            String sortDirection,
            Long brand,
            String wallpaperType,
            String wallpaperSize);

    PageResponse<ProductDto> findLastAdded(
            int page,
            int size,
            String sortBy,
            String sortDirection,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Long categoryId,
            Long brand,
            String wallpaperType,
            String wallpaperSize);




    PageResponse<ProductDto> findByColourSlug(
            String slug,
            int page,
            int size,
            String sortBy,
            String sortDirection,
            Long brand,
            String wallpaperType,
            String wallpaperSize,
            BigDecimal maxPrice,
            BigDecimal minPrice);

    PageResponse<ProductDto> findByDesignSlug(
            String slug,
            int page,
            int size,
            String sortBy,
            String sortDirection,
            Long brand,
            String wallpaperType,
            String wallpaperSize,
            BigDecimal maxPrice,
            BigDecimal minPrice);

    PageResponse<ProductDto> findByRoomSlug(
            String slug,
            int page,
            int size,
            String sortBy,
            String sortDirection,
            Long brand,
            String wallpaperType,
            String wallpaperSize,
            BigDecimal maxPrice,
            BigDecimal minPrice);

    PageResponse<ProductDto> findFeaturedProducts(
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            int page,
            int size,
            String sortBy,
            String sortDirection,
            Long brand,
            String wallpaperType,
            String wallpaperSize);
}
