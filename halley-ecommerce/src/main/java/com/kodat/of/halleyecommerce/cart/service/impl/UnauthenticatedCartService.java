package com.kodat.of.halleyecommerce.cart.service.impl;

import com.kodat.of.halleyecommerce.cart.Cart;
import com.kodat.of.halleyecommerce.cart.CartItem;
import com.kodat.of.halleyecommerce.cart.CartItemRepository;
import com.kodat.of.halleyecommerce.cart.CartRepository;
import com.kodat.of.halleyecommerce.dto.cart.CartDto;
import com.kodat.of.halleyecommerce.dto.cart.CartItemDto;
import com.kodat.of.halleyecommerce.exception.CartItemNotFoundException;
import com.kodat.of.halleyecommerce.exception.CartNotFoundException;
import com.kodat.of.halleyecommerce.mapper.cart.CartMapper;
import com.kodat.of.halleyecommerce.product.Product;
import com.kodat.of.halleyecommerce.util.CookieUtils;
import com.kodat.of.halleyecommerce.util.RedisUtils;
import com.kodat.of.halleyecommerce.util.UnauthenticatedUtils;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UnauthenticatedCartService {

    private final UnauthenticatedUtils unauthenticatedUtils;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final RedisUtils redisUtils;
    private final CookieUtils cookieUtils;

    public UnauthenticatedCartService(UnauthenticatedUtils unauthenticatedUtils, CartRepository cartRepository, CartItemRepository cartItemRepository, RedisUtils redisUtils, CookieUtils cookieUtils) {
        this.unauthenticatedUtils = unauthenticatedUtils;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.redisUtils = redisUtils;
        this.cookieUtils = cookieUtils;
    }


    // Add a product to the cart
    public void addToCart(Long productId) {
        Cart cart = unauthenticatedUtils.getOrCreateCart();
        CartDto sessionCart = redisUtils.getCartFromRedis(cart.getCartToken());
        if (sessionCart == null) {
            sessionCart = new CartDto();
        }
        unauthenticatedUtils.addProduct(sessionCart, productId);
    }

    // Get the cart
    public CartDto getCart() {
        String cartToken = cookieUtils.getOrCreateCartToken();
        Cart cart = cartRepository.findByCartToken(cartToken)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setCartToken(cartToken);
                    redisUtils.saveCartToRedis(cartToken, CartMapper.INSTANCE.toCartDto(newCart));
                    return cartRepository.save(newCart);
                });
        return CartMapper.INSTANCE.toCartDto(cart);
    }

    // Remove a product from the cart
    public void removeCartItemFromCart(Long productId) {
        Cart cart = unauthenticatedUtils.getOrCreateCart();
        CartDto sessionCart = redisUtils.getCartFromRedis(cart.getCartToken());
        if (sessionCart == null) {
            throw new CartNotFoundException("Cart does not exist in the session");
        }
        CartItemDto existingItem = unauthenticatedUtils.findItemByProductId(sessionCart, productId);
        if (existingItem != null) {
            sessionCart.getItems().remove(existingItem);
            redisUtils.saveCartToRedis(cart.getCartToken(), sessionCart);
        } else {
            throw new CartItemNotFoundException("Product does not exist in session");
        }
    }

    // Decrease product quantity for unauthenticated user
    public void decreaseProductQuantityForUnauthenticatedUser(Long productId) {
        Cart cart = unauthenticatedUtils.getOrCreateCart();
        CartDto sessionCart = redisUtils.getCartFromRedis(cart.getCartToken());
        if (sessionCart == null) {
            throw new CartNotFoundException("Cart does not exist in session");
        }
        CartItemDto cartItemDto = unauthenticatedUtils.findItemByProductId(sessionCart, productId);
        if (cartItemDto == null) {
            throw new CartItemNotFoundException("Product does not exist in session");
        }
        int newQuantity = cartItemDto.getQuantity() - 1;
        if (newQuantity <= 0) {
            sessionCart.getItems().remove(cartItemDto);
            CartItem cartItem = cartItemRepository.findByProductIdAndCartId(productId, cart.getId());
            if (cartItem != null) {
                cartItemRepository.delete(cartItem);
            }
        } else {
            cartItemDto.setQuantity(newQuantity);
        }
        unauthenticatedUtils.updateCartItem(cart, cartItemDto);
        redisUtils.saveCartToRedis(cart.getCartToken(), sessionCart);
        sessionCart.setId(cart.getId());
    }

    public void increaseQuantityForUnauthenticated(Long productId) {
        Cart cart = unauthenticatedUtils.getOrCreateCart();
        CartDto sessionCart = redisUtils.getCartFromRedis(cart.getCartToken());
        if (sessionCart == null) {
            throw new CartNotFoundException("Cart does not exist in session");
        }
        CartItemDto existingItem = unauthenticatedUtils.findItemByProductId(sessionCart, productId);
        if (existingItem == null) {
            throw new CartItemNotFoundException("Product does not exist in session");
        }
        Product product = unauthenticatedUtils.fetchProductById(productId);
        unauthenticatedUtils.updateExistingCartItem(cart, existingItem, product.getStock());
        redisUtils.saveCartToRedis(cart.getCartToken(), sessionCart);
        sessionCart.setId(cart.getId());
    }

    // Clear the cart
    public void removeAllCartItemFromCart() {
        Cart cart = unauthenticatedUtils.getOrCreateCart();
        CartDto sessionCart = redisUtils.getCartFromRedis(cart.getCartToken());
        if (sessionCart == null) {
            throw new CartNotFoundException("Cart does not exist in session");
        }
        if (sessionCart.getItems().isEmpty()) {
            throw new CartNotFoundException("Cart is already empty");
        }
        cartItemRepository.deleteAllByCartId(cart.getId());
        sessionCart.getItems().clear();
        redisUtils.clearCartFromRedis(cart.getCartToken());
    }

    // Check if cart is empty for unauthenticated user
    public Boolean isEmptyForUnauthenticated() {
        Cart cart = unauthenticatedUtils.getOrCreateCart();
        CartDto sessionCart = redisUtils.getCartFromRedis(cart.getCartToken());
        return sessionCart == null || sessionCart.getItems().isEmpty();
    }

    public void removeSelectedCartItemsFromCart(List<Long> productIds) {
        Cart cart = unauthenticatedUtils.getOrCreateCart();
        CartDto sessionCart = redisUtils.getCartFromRedis(cart.getCartToken());
        if (sessionCart == null) {
            throw new CartNotFoundException("Cart does not exist in the session");
        }
        for (Long productId : productIds) {
            CartItem cartItem = cartItemRepository.findByProductIdAndCartId(productId, cart.getId());
            if (cartItem != null) {
                cartItemRepository.delete(cartItem);
                sessionCart.getItems().removeIf(item -> item.getId().equals(cartItem.getId()));
            } else {
                throw new CartItemNotFoundException("Product with ID " + productId + " does not exist in session");

            }
        }
        redisUtils.saveCartToRedis(cart.getCartToken(), sessionCart);

    }
}

