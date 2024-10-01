package com.kodat.of.halleyecommerce.mapper.product;

import com.kodat.of.halleyecommerce.dto.product.ProductDto;
import com.kodat.of.halleyecommerce.category.Category;
import com.kodat.of.halleyecommerce.product.Product;


public class ProductMapper {
    public static Product toProduct(ProductDto productDto , Category category) {
        return Product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .stock(productDto.getStock())
                .productCode(productDto.getProductCode())
                .imageUrl(productDto.getImageUrl())
                .category(category)
                .build();
    }
    public static ProductDto toProductDto(Product product) {
        return ProductDto.builder()
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .productCode(product.getProductCode())
                .imageUrl(product.getImageUrl())
                .categoryId(product.getCategory().getId())
                .build();
    }
    public static Product updateProductFromDto(ProductDto productDto, Product existingProduct , Category category) {
        existingProduct.setName(productDto.getName());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setStock(productDto.getStock());
        existingProduct.setProductCode(productDto.getProductCode());
        existingProduct.setImageUrl(productDto.getImageUrl());
        existingProduct.setCategory(category);
        return existingProduct;
    }
}