package com.kodat.of.halleyecommerce.cart.service.impl;

import com.kodat.of.halleyecommerce.cart.service.CartService;
import com.kodat.of.halleyecommerce.dto.cart.AddToCartRequest;
import com.kodat.of.halleyecommerce.dto.cart.CartDto;
import com.kodat.of.halleyecommerce.dto.cart.CartSummaryDto;
import com.kodat.of.halleyecommerce.util.CartManagerUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;


@Service
public class CartServiceImpl implements CartService {

    private final CartManagerUnauthenticatedService unauthenticatedService;
    private final CartManagerAuthenticatedService authenticatedService;
    private final CartManagerUtils cartManagerUtils;

    public CartServiceImpl(CartManagerUnauthenticatedService unauthenticatedService, CartManagerAuthenticatedService authenticatedService, CartManagerUtils cartManagerUtils) {
        this.unauthenticatedService = unauthenticatedService;
        this.authenticatedService = authenticatedService;
        this.cartManagerUtils = cartManagerUtils;
    }


    @Override
    public CartDto handleGetCart(Authentication connectedUser, HttpSession session) {
        if (isAuthenticated(connectedUser)) {
            return handleAuthenticatedCart(connectedUser, session);
        } else {
            return handleUnauthenticatedCart(session);
        }
    }

    private CartDto handleAuthenticatedCart(Authentication connectedUser, HttpSession session) {
        CartDto cartDto = authenticatedService.getCart(connectedUser);
        CartDto sessionCart = (CartDto) session.getAttribute("cart");
        if (sessionCart != null && !sessionCart.getItems().isEmpty()) {
            cartDto = cartManagerUtils.mergeCarts(cartDto, sessionCart.getItems());
            session.removeAttribute("cart");
        }
        return cartDto;
    }

    private CartDto handleUnauthenticatedCart(HttpSession session) {
        CartDto cartDto = (CartDto) session.getAttribute("cart");
        return cartDto != null ? cartDto : new CartDto();
    }


    @Transactional
    @Override
    public CartDto addToCart(Authentication connectedUser, HttpSession session, AddToCartRequest request) {
        if (isAuthenticated(connectedUser)) {
            return authenticatedService.addToCartForAuthenticatedUser(request, connectedUser);
        } else {
            return unauthenticatedService.addToCartForUnauthenticatedUser(request, session);
        }
    }

    @Override
    public CartDto removeFromCart(Authentication connectedUser, HttpSession session, Long productId) {
        if (isAuthenticated(connectedUser)) {
            return authenticatedService.removeFromAuthenticatedUser(productId, connectedUser);
        } else {
            return unauthenticatedService.removeFromUnAuthenticatedUser(productId, session);
        }
    }


    @Override
    public CartDto decreaseProductQuantity(Authentication connectedUser, HttpSession session, Long productId, Integer quantity) {
        if (isAuthenticated(connectedUser)) {
            return authenticatedService.decreaseProductQuantityForAuthenticatedUser(productId, quantity, connectedUser);
        } else {
            return unauthenticatedService.decreaseProductQuantityForUnauthenticatedUser(productId, quantity, session);
        }
    }

    @Transactional
    @Override
    public void clearCart(Authentication connectedUser, HttpSession session) {
        if (isAuthenticated(connectedUser)) {
            authenticatedService.clearAuthenticatedCart(connectedUser);
        } else {
            unauthenticatedService.clearUnauthenticatedCart(session);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public CartSummaryDto getCartSummary(Authentication connectedUser, HttpSession session) {
        if (isAuthenticated(connectedUser)) {
            return authenticatedService.getCartSummaryAuthenticated(connectedUser);
        } else {
            return unauthenticatedService.getCartSummaryUnauthenticated(session);
        }
    }

    @Override
    public Boolean isEmptyCart(Authentication connectedUser, HttpSession session) {
        if (isAuthenticated(connectedUser)) {
            return authenticatedService.isEmptyForAuthenticated(connectedUser);
        } else {
            return unauthenticatedService.isEmptyForUnauthenticated(session);
        }
    }

    @Transactional
    @Override
    public CartDto updateAllQuantities(Authentication connectedUser, HttpSession session, Map<Long, Integer> productQuantities) {
        if (isAuthenticated(connectedUser)) {
            return authenticatedService.updateQuantitiesForAuthenticated(connectedUser, productQuantities);
        } else {
            return unauthenticatedService.updateQuantitiesForUnauthenticated(session, productQuantities);
        }
    }

    private boolean isAuthenticated(Authentication connectedUser) {
        return connectedUser != null && connectedUser.isAuthenticated();
    }


}
