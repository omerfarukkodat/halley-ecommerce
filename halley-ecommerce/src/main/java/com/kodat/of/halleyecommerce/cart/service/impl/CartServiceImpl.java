package com.kodat.of.halleyecommerce.cart.service.impl;

import com.kodat.of.halleyecommerce.cart.service.CartService;
import com.kodat.of.halleyecommerce.dto.cart.CartDto;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final UnauthenticatedCartService unauthenticatedService;
    private final AuthenticatedCartService authenticatedService;

    public CartServiceImpl(UnauthenticatedCartService unauthenticatedService, AuthenticatedCartService authenticatedService) {
        this.unauthenticatedService = unauthenticatedService;
        this.authenticatedService = authenticatedService;
    }

    @Override
    public CartDto getCart(Authentication connectedUser) {
        if (isAuthenticated(connectedUser)) {
            return authenticatedService.getCart(connectedUser);
        } else {
            return unauthenticatedService.getCart();
        }
    }

    @Transactional
    @Override
    public void addToCart(Authentication connectedUser, Long productId) {
        if (isAuthenticated(connectedUser)) {
            authenticatedService.addToCart(connectedUser, productId);
        } else {
            unauthenticatedService.addToCart(productId);
        }
    }

    @Override
    public void removeCartItemFromCart(Authentication connectedUser, Long productId) {
        if (isAuthenticated(connectedUser)) {
            authenticatedService.removeCartItemFromCart(productId, connectedUser);
        } else {
            unauthenticatedService.removeCartItemFromCart(productId);
        }
    }

    @Override
    public void decreaseProductQuantity(Authentication connectedUser, Long productId) {
        if (isAuthenticated(connectedUser)) {
            authenticatedService.decreaseProductQuantityForAuthenticatedUser(productId, connectedUser);
        } else {
            unauthenticatedService.decreaseProductQuantityForUnauthenticatedUser(productId);
        }
    }

    @Transactional
    @Override
    public void removeAllCartItemFromCart(Authentication connectedUser) {
        if (isAuthenticated(connectedUser)) {
            authenticatedService.removeAllCartItemFromCart(connectedUser);
        } else {
            unauthenticatedService.removeAllCartItemFromCart();
        }
    }

    @Override
    public Boolean isEmptyCart(Authentication connectedUser) {
        if (isAuthenticated(connectedUser)) {
            return authenticatedService.isEmptyForAuthenticated(connectedUser);
        } else {
            return unauthenticatedService.isEmptyForUnauthenticated();
        }
    }

    @Override
    public void increaseProductQuantity(Authentication connectedUser, Long productId) {
        if (isAuthenticated(connectedUser)) {
            authenticatedService.increaseQuantityForAuthenticated(connectedUser, productId);
        } else {
            unauthenticatedService.increaseQuantityForUnauthenticated(productId);
        }
    }

    @Override
    public void removeSelectedCartItemsFromCart(List<Long> productIds, Authentication connectedUser) {
        if (isAuthenticated(connectedUser)) {
            authenticatedService.removeSelectedCartItemsFromCart(productIds, connectedUser);
        } else {
            unauthenticatedService.removeSelectedCartItemsFromCart(productIds);
        }
    }

    private boolean isAuthenticated(Authentication connectedUser) {
        return connectedUser != null && connectedUser.isAuthenticated();
    }
}
