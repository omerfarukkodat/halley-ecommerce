package com.kodat.of.halleyecommerce.validator;

import com.kodat.of.halleyecommerce.cart.Cart;
import com.kodat.of.halleyecommerce.cart.CartRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class CartValidator {
    private final CartRepository cartRepository;

    public CartValidator(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }


    public Cart validateCartAndUser(Authentication connectedUser) {
    String username = connectedUser.getName();
    return cartRepository.findByUser_Email(username)
            .orElseThrow(() -> new IllegalArgumentException("The user does not have permission to access this cart or the cart was not found."));
    }
}
