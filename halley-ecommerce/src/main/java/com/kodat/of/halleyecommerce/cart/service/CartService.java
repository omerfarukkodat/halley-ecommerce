package com.kodat.of.halleyecommerce.cart.service;

import com.kodat.of.halleyecommerce.dto.cart.AddToCartRequest;
import com.kodat.of.halleyecommerce.dto.cart.CartDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;

public interface CartService {

    CartDto handleGetCart(Authentication connectedUser, HttpSession session);

    CartDto addToCart(Authentication connectedUser, HttpSession session, AddToCartRequest request);

    CartDto removeFromCart(Authentication connectedUser, HttpSession session, Long productId);

    CartDto decreaseProductQuantity(Authentication connectedUser, HttpSession session, Long productId, Integer quantity);

    void clearCart(Authentication connectedUser, HttpSession session);
}
