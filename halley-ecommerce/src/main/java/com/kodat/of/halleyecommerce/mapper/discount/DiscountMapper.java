package com.kodat.of.halleyecommerce.mapper.discount;

import com.kodat.of.halleyecommerce.discount.Discount;
import com.kodat.of.halleyecommerce.dto.discount.DiscountDto;
import com.kodat.of.halleyecommerce.product.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DiscountMapper {



    public static Discount toDiscount(DiscountDto discountDto, List<Product> products) {

        Discount discount = Discount.builder()
                .id(discountDto.getId())
                .discountPercentage(discountDto.getDiscountPercentage())
                .startDate(discountDto.getStartDate())
                .endDate(discountDto.getEndDate())
                .products(null)
                .build();

        List<Product> selectedProducts = products.stream()
                .filter(product -> discountDto.getProductIds()
                .contains(product.getId()))
                .peek(product -> product.setDiscount(discount))
                .toList();

        discount.setProducts(selectedProducts);
        return discount;
    }

    public static DiscountDto toDiscountDto(Discount discount) {

        List<Long> productIds = discount.getProducts().stream()
                .map(Product::getId)
                .toList();

        return DiscountDto.builder()
                .id(discount.getId())
                .discountPercentage(discount.getDiscountPercentage())
                .startDate(discount.getStartDate())
                .endDate(discount.getEndDate())
                .productIds(productIds)
                .build();
    }
    public static Discount updateDiscountFromDto(DiscountDto discountDto, Discount discount, List<Product> products ) {

        discount.setDiscountPercentage(discountDto.getDiscountPercentage());
        discount.setStartDate(discountDto.getStartDate());
        discount.setEndDate(discountDto.getEndDate());
        for (Product product : products) {
            product.setDiscount(discount);
        }
        discount.setProducts(products);
        return discount;
    }


}
