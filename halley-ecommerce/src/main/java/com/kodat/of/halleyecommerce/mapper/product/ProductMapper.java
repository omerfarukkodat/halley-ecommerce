package com.kodat.of.halleyecommerce.mapper.product;

import com.kodat.of.halleyecommerce.dto.product.ProductDto;
import com.kodat.of.halleyecommerce.category.Category;
import com.kodat.of.halleyecommerce.product.Product;

import java.util.Set;
import java.util.stream.Collectors;


public class ProductMapper {
    public static Product toProduct(ProductDto productDto , Set<Category> categories) {
        return Product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .stock(productDto.getStock())
                .productCode(productDto.getProductCode())
                .imageUrl(productDto.getImageUrl())
                .categories(categories)
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
                .categoryIds(product.getCategories().stream().map(Category::getId).collect(Collectors.toSet()))
                .build();
    }
    public static Product updateProductFromDto(ProductDto productDto, Product existingProduct , Set<Category> categories) {
        existingProduct.setName(productDto.getName());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setStock(productDto.getStock());
        existingProduct.setProductCode(productDto.getProductCode());
        existingProduct.setImageUrl(productDto.getImageUrl());
        existingProduct.setCategories(categories);
        return existingProduct;
    }
}
