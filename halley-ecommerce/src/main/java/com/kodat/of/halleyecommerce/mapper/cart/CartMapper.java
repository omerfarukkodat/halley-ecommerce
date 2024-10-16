package com.kodat.of.halleyecommerce.mapper.cart;

import com.kodat.of.halleyecommerce.cart.Cart;
import com.kodat.of.halleyecommerce.cart.CartItem;
import com.kodat.of.halleyecommerce.dto.cart.CartDto;
import com.kodat.of.halleyecommerce.dto.cart.CartItemDto;
import com.kodat.of.halleyecommerce.dto.cart.ProductResponseDto;
import com.kodat.of.halleyecommerce.product.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CartMapper {
    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

    CartDto toCartDto(Cart cart);
    Cart toCart(CartDto cartDto);
    CartItem toCartItem(CartItemDto cartItemDto);
    CartItemDto toCartItemDto(CartItem cartItem);
    ProductResponseDto toProductResponseDto(Product product);
    Product toProduct(ProductResponseDto productResponseDto);
    List<CartItemDto> toCartItemDtoList(List<CartItem> cartItemList);
    List<CartItem> toCartItemList(List<CartItemDto> cartItemDtoList);

}