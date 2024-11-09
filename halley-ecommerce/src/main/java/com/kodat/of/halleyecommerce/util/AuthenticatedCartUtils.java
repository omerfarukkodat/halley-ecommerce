package com.kodat.of.halleyecommerce.util;

import com.kodat.of.halleyecommerce.cart.Cart;
import com.kodat.of.halleyecommerce.cart.CartItem;
import com.kodat.of.halleyecommerce.cart.CartItemRepository;
import com.kodat.of.halleyecommerce.cart.CartRepository;
import com.kodat.of.halleyecommerce.dto.cart.CartDto;
import com.kodat.of.halleyecommerce.dto.cart.CartItemDto;
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
    private final RedisUtils redisUtils;

    public AuthenticatedCartUtils(CartRepository cartRepository, CartItemRepository cartItemRepository, UnauthenticatedUtils unauthenticatedUtils, RoleValidator roleValidator, StockUtils stockUtils, RedisUtils redisUtils) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.unauthenticatedUtils = unauthenticatedUtils;
        this.roleValidator = roleValidator;
        this.stockUtils = stockUtils;
        this.redisUtils = redisUtils;
    }

    public void mergeCarts(Authentication connectedUser) {
        roleValidator.verifyUserRole(connectedUser);
        CustomUserDetails customUserDetails = (CustomUserDetails) connectedUser.getPrincipal();
        User user = customUserDetails.getUser();
        Cart sessionCart = unauthenticatedUtils.getOrCreateCart();
        Cart userCart = cartRepository.findByUser(user)
                .orElseGet(() -> createNewCartForUser(user));
        for (CartItem sessionItem : sessionCart.getItems()) {
            mergeCartItem(userCart, sessionItem);
        }
        sessionCart.getItems().clear();
        redisUtils.clearCartFromRedis(sessionCart.getCartToken());
        cartRepository.delete(sessionCart);
        cartRepository.save(userCart);
        CartMapper.INSTANCE.toCartDto(userCart);
    }

    private void mergeCartItem(Cart userCart, CartItem sessionItem) {
        CartItem existingItem = findExistingCartItem(userCart, sessionItem.getProduct().getId());

        if (existingItem != null) {
            int combinedQuantity = Math.min(
                    existingItem.getQuantity() + sessionItem.getQuantity(),
                    sessionItem.getProduct().getStock()
            );
            if (combinedQuantity != existingItem.getQuantity()) {
                existingItem.setQuantity(combinedQuantity);
                cartItemRepository.save(existingItem);
            }
        }
        else {
            int quantityToAdd = Math.min(sessionItem.getQuantity(), sessionItem.getProduct().getStock());
            CartItem newUserCartItem = CartItem.builder()
                    .product(sessionItem.getProduct())
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

    public CartItemDto findItemByProductId(CartDto cartDto, Long productId) {
        return cartDto.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
    }
}
