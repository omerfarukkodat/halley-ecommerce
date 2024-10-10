package com.kodat.of.halleyecommerce.mapper.discount;

import com.kodat.of.halleyecommerce.discount.Discount;
import com.kodat.of.halleyecommerce.dto.discount.DiscountDto;
import com.kodat.of.halleyecommerce.product.Product;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DiscountMapper {
    public static Discount toDiscount(DiscountDto discountDto, List<Product> products) {

        List<Product> selectedProducts = products.stream()
                .filter(product -> discountDto.getProductIds()
                        .contains(product.getId()))
                .toList();

        return Discount.builder()
                .id(discountDto.getId())
                .discountPercentage(discountDto.getDiscountPercentage())
                .startDate(discountDto.getStartDate())
                .endDate(discountDto.getEndDate())
                .products(selectedProducts)
                .build();
    }

    public static DiscountDto toDiscountDto(Discount discount) {

        Set<Long> productIds = discount.getProducts().stream()
                .map(Product::getId)
                .collect(Collectors.toSet());


        return DiscountDto.builder()
                .id(discount.getId())
                .discountPercentage(discount.getDiscountPercentage())
                .startDate(discount.getStartDate())
                .endDate(discount.getEndDate())
                .productIds(productIds)
                .build();
    }



}
