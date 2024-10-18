package com.kodat.of.halleyecommerce.cart.service;

import com.kodat.of.halleyecommerce.dto.cart.AddToCartRequest;
import com.kodat.of.halleyecommerce.dto.cart.CartDto;
import com.kodat.of.halleyecommerce.dto.cart.CartSummaryDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;

import java.util.Map;

public interface CartService {

    CartDto handleGetCart(Authentication connectedUser, HttpSession session);

    CartDto addToCart(Authentication connectedUser, HttpSession session, AddToCartRequest request);

    CartDto removeFromCart(Authentication connectedUser, HttpSession session, Long productId);

    CartDto decreaseProductQuantity(Authentication connectedUser, HttpSession session, Long productId, Integer quantity);

    void clearCart(Authentication connectedUser, HttpSession session);

    CartSummaryDto getCartSummary(Authentication connectedUser, HttpSession session);

    Boolean isEmptyCart(Authentication connectedUser, HttpSession session);

    CartDto updateAllQuantities(Authentication connectedUser, HttpSession session , Map<Long,Integer> productQuantities);
}
