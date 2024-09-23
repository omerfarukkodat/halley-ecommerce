package com.kodat.of.halleyecommerce.mapper.product;

import com.kodat.of.halleyecommerce.dto.product.ProductDto;
import com.kodat.of.halleyecommerce.product.entity.Category;
import com.kodat.of.halleyecommerce.product.entity.Product;

public class ProductMapper {
    public static Product toProduct(ProductDto productDto , Category category) {
        return Product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .stock(productDto.getStock())
                .imageUrl(productDto.getImageUrl())
                .category(category)
                .build();
    }
}
