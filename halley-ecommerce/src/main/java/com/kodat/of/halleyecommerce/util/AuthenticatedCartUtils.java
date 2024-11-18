package com.kodat.of.halleyecommerce.util;

import com.kodat.of.halleyecommerce.cart.Cart;
import com.kodat.of.halleyecommerce.cart.CartItem;
import com.kodat.of.halleyecommerce.cart.CartItemRepository;
import com.kodat.of.halleyecommerce.cart.CartRepository;
import com.kodat.of.halleyecommerce.exception.InsufficientStockException;
import com.kodat.of.halleyecommerce.mapper.cart.CartMapper;
import com.kodat.of.halleyecommerce.product.Product;
import com.kodat.of.halleyecommerce.user.CustomUserDetails;
import com.kodat.of.halleyecommerce.user.User;
import com.kodat.of.halleyecommerce.validator.RoleValidator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedCartUtils {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UnauthenticatedUtils unauthenticatedUtils;
    private final RoleValidator roleValidator;
    private final StockUtils stockUtils;

    public AuthenticatedCartUtils(CartRepository cartRepository, CartItemRepository cartItemRepository, UnauthenticatedUtils unauthenticatedUtils, RoleValidator roleValidator, StockUtils stockUtils) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.unauthenticatedUtils = unauthenticatedUtils;
        this.roleValidator = roleValidator;
        this.stockUtils = stockUtils;
    }


    public void mergeCarts(Authentication connectedUser) {
        roleValidator.verifyUserRole(connectedUser);
        CustomUserDetails customUserDetails = (CustomUserDetails) connectedUser.getPrincipal();
        User user = customUserDetails.getUser();
        Cart nonMemberCart = unauthenticatedUtils.getOrCreateCart();
        Cart userCart = cartRepository.findByUser(user)
                .orElseGet(() -> createNewCartForUser(user));
        for (CartItem nonMemberCartItem : nonMemberCart.getItems()) {
            mergeCartItem(userCart, nonMemberCartItem);
        }
        nonMemberCart.getItems().clear();
        cartRepository.delete(nonMemberCart);
        cartRepository.save(userCart);
        CartMapper.INSTANCE.toCartDto(userCart);
    }

    private void mergeCartItem(Cart userCart, CartItem nonMemberCartItem) {
        CartItem existingItem = findExistingCartItem(userCart, nonMemberCartItem.getProduct().getId());

        if (existingItem != null) {
            int combinedQuantity = Math.min(
                    existingItem.getQuantity() + nonMemberCartItem.getQuantity(),
                    nonMemberCartItem.getProduct().getStock()
            );
            if (combinedQuantity != existingItem.getQuantity()) {
                existingItem.setQuantity(combinedQuantity);
                cartItemRepository.save(existingItem);
            }
        }
        else {
            int quantityToAdd = Math.min(nonMemberCartItem.getQuantity(), nonMemberCartItem.getProduct().getStock());
            CartItem newUserCartItem = CartItem.builder()
                    .product(nonMemberCartItem.getProduct())
                    .quantity(quantityToAdd)
                    .cart(userCart)
                    .build();
            cartItemRepository.save(newUserCartItem);
            userCart.getItems().add(newUserCartItem);
        }
    }

    public void addOrUpdateCartItem(Cart cart, Product product) {
        CartItem existingCartItem = findExistingCartItem(cart, product.getId());
        if (existingCartItem != null) {
            int newQuantity = existingCartItem.getQuantity() + 1;
            if (stockUtils.isStockAvailable(product.getId(), newQuantity)) {
                existingCartItem.setQuantity(newQuantity);
                cartItemRepository.save(existingCartItem);
            } else {
                throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
            }
        } else {
            if (product.getStock() > 0) {
                CartItem newCartItem = CartItem.builder()
                        .product(product)
                        .quantity(1)
                        .cart(cart)
                        .build();
                cart.getItems().add(newCartItem);
                cartItemRepository.save(newCartItem);
            } else {
                throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
            }
        }
    }

    public CartItem findExistingCartItem(Cart userCart, Long productId) {
        return userCart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    private Cart createNewCartForUser(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }

}
