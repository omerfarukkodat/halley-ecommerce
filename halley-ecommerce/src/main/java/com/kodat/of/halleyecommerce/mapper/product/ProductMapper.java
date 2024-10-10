package com.kodat.of.halleyecommerce.mapper.product;

import com.kodat.of.halleyecommerce.dto.product.ProductDto;
import com.kodat.of.halleyecommerce.category.Category;
import com.kodat.of.halleyecommerce.product.Product;

import java.util.Set;
import java.util.stream.Collectors;


public class ProductMapper {
    public static Product toProduct(ProductDto productDto , Set<Category> categories , String slug) {
        return Product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .originalPrice(productDto.getOriginalPrice())
                .discountedPrice(productDto.getDiscountedPrice())
                .stock(productDto.getStock())
                .productCode(productDto.getProductCode())
                .imageUrl(productDto.getImageUrl())
                .categories(categories)
                .slug(slug)
                .isFeatured(productDto.isFeatured())
                .build();
    }
    public static ProductDto toProductDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .originalPrice(product.getOriginalPrice())
                .discountedPrice(product.getDiscountedPrice())
                .stock(product.getStock())
                .productCode(product.getProductCode())
                .imageUrl(product.getImageUrl())
                .categoryIds(product.getCategories().stream().map(Category::getId).collect(Collectors.toSet()))
                .slug(product.getSlug())
                .isFeatured(product.isFeatured())
                .build();
    }
    public static Product updateProductFromDto(ProductDto productDto, Product existingProduct , Set<Category> categories) {
        existingProduct.setName(productDto.getName());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setOriginalPrice(productDto.getOriginalPrice());
        existingProduct.setDiscountedPrice(productDto.getDiscountedPrice());
        existingProduct.setStock(productDto.getStock());
        existingProduct.setProductCode(productDto.getProductCode());
        existingProduct.setImageUrl(productDto.getImageUrl());
        existingProduct.setCategories(categories);
        existingProduct.setSlug(productDto.getSlug());
        existingProduct.setFeatured(productDto.isFeatured());
        return existingProduct;
    }
}
