package com.kodat.of.halleyecommerce.cart;

import com.kodat.of.halleyecommerce.cart.service.CartManagerService;
import com.kodat.of.halleyecommerce.cart.service.CartService;
import com.kodat.of.halleyecommerce.dto.cart.AddToCartRequest;
import com.kodat.of.halleyecommerce.dto.cart.CartDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CartServiceImpl implements CartService {

    private final CartManagerService cartManagerService;

    public CartServiceImpl(CartManagerService cartManagerService) {
        this.cartManagerService = cartManagerService;
    }


    @Override
    public CartDto handleGetCart(Authentication connectedUser, HttpSession session) {
        CartDto cartDto;
        if (connectedUser != null && connectedUser.isAuthenticated()) {
            cartDto = cartManagerService.getCart(connectedUser);
            CartDto sessionCart = (CartDto) session.getAttribute("cart");
            if (sessionCart != null && !sessionCart.getItems().isEmpty()) {
                cartDto = cartManagerService.mergeCarts(cartDto, sessionCart.getItems());
                session.removeAttribute("cart");
            }
        } else {
            cartDto = (CartDto) session.getAttribute("cart");
            if (cartDto == null) {
                cartDto = new CartDto();
            }
        }
        return cartDto;
    }
    @Transactional
    @Override
    public CartDto addToCart(Authentication connectedUser, HttpSession session, AddToCartRequest request) {
        if (connectedUser != null && connectedUser.isAuthenticated()) {
            return cartManagerService.addToCartForAuthenticatedUser(request, connectedUser);
        } else {
            return cartManagerService.addToCartForUnauthenticatedUser(request, session);
        }
    }

    @Override
    public CartDto removeFromCart(Authentication connectedUser, HttpSession session, Long productId) {
        if (connectedUser != null && connectedUser.isAuthenticated()) {
            return cartManagerService.removeFromAuthenticatedUser(productId,connectedUser);
        }else {
            return cartManagerService.removeFromUnAuthenticatedUser(productId,session);
        }
    }

    @Override
    public CartDto decreaseProductQuantity(Authentication connectedUser, HttpSession session, Long productId, Integer quantity) {
        return cartManagerService.decreaseProductQuantity(connectedUser,session,productId,quantity);
    }

    @Transactional
    @Override
    public void clearCart(Authentication connectedUser, HttpSession session) {
        if (connectedUser != null && connectedUser.isAuthenticated()) {
            cartManagerService.clearAuthenticatedCart(connectedUser);
        }else {
            cartManagerService.clearUnauthenticatedCart(session);
        }
    }


}
