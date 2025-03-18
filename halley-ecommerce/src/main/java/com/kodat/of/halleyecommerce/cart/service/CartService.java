package com.kodat.of.halleyecommerce.cart.service;

import com.kodat.of.halleyecommerce.dto.cart.CartDto;
import com.kodat.of.halleyecommerce.dto.cart.CartSummaryDto;
import org.springframework.security.core.Authentication;

import java.util.List;


public interface CartService {

    CartDto getCart(Authentication connectedUser);

    void addToCart(Authentication connectedUser,  Long productId);

    void removeCartItemFromCart(Authentication connectedUser, Long productId);

    void decreaseProductQuantity(Authentication connectedUser, Long productId);

    void removeAllCartItemFromCart(Authentication connectedUser);

    Boolean isEmptyCart(Authentication connectedUser);

    void increaseProductQuantity(Authentication connectedUser , Long productId);

    void removeSelectedCartItemsFromCart(List<Long> productIds, Authentication connectedUser);

    Integer getProductQuantity(Long productId, Authentication connectedUser);

    CartSummaryDto getCartSummary(Authentication connectedUser);
}
