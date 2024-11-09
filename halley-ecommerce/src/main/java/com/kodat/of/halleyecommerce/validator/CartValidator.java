package com.kodat.of.halleyecommerce.validator;

import com.kodat.of.halleyecommerce.cart.Cart;
import com.kodat.of.halleyecommerce.cart.CartRepository;
import com.kodat.of.halleyecommerce.user.CustomUserDetails;
import com.kodat.of.halleyecommerce.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class CartValidator {
    private final CartRepository cartRepository;
    private final RoleValidator roleValidator;

    public CartValidator(CartRepository cartRepository, RoleValidator roleValidator) {
        this.cartRepository = cartRepository;
        this.roleValidator = roleValidator;
    }


    public Cart validateCartAndUser(Authentication connectedUser) {
        roleValidator.verifyUserRole(connectedUser);
        String username = connectedUser.getName();
        return cartRepository.findByUser_Email(username)
                .orElseGet(() -> createNewCartForUser(connectedUser));
    }

    public Cart createNewCartForUser(Authentication connectedUser) {
        CustomUserDetails customUserDetails = (CustomUserDetails) connectedUser.getPrincipal();
        User user = customUserDetails.getUser();
        Cart cart = Cart.builder()
                .user(user)
                .build();
        return cartRepository.save(cart);
    }

}
