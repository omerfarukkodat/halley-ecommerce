package com.kodat.of.halleyecommerce.cart.service.impl;

import com.kodat.of.halleyecommerce.cart.Cart;
import com.kodat.of.halleyecommerce.cart.CartItem;
import com.kodat.of.halleyecommerce.cart.CartItemRepository;
import com.kodat.of.halleyecommerce.cart.CartRepository;
import com.kodat.of.halleyecommerce.dto.cart.CartDto;
import com.kodat.of.halleyecommerce.exception.*;
import com.kodat.of.halleyecommerce.mapper.cart.CartMapper;
import com.kodat.of.halleyecommerce.product.Product;
import com.kodat.of.halleyecommerce.user.CustomUserDetails;
import com.kodat.of.halleyecommerce.user.User;
import com.kodat.of.halleyecommerce.util.AuthenticatedCartUtils;
import com.kodat.of.halleyecommerce.util.StockUtils;
import com.kodat.of.halleyecommerce.validator.CartValidator;
import com.kodat.of.halleyecommerce.validator.ProductValidator;
import com.kodat.of.halleyecommerce.validator.RoleValidator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticatedCartService {
    private final CartRepository cartRepository;
    private final RoleValidator roleValidator;
    private final CartValidator cartValidator;
    private final CartItemRepository cartItemRepository;
    private final ProductValidator productValidator;
    private final StockUtils stockUtils;
    private final AuthenticatedCartUtils authenticatedCartUtils;

    public AuthenticatedCartService(CartRepository cartRepository, RoleValidator roleValidator, CartValidator cartValidator, CartItemRepository cartItemRepository, ProductValidator productValidator, StockUtils stockUtils, AuthenticatedCartUtils authenticatedCartUtils) {
        this.cartRepository = cartRepository;
        this.roleValidator = roleValidator;
        this.cartValidator = cartValidator;
        this.cartItemRepository = cartItemRepository;
        this.productValidator = productValidator;
        this.stockUtils = stockUtils;
        this.authenticatedCartUtils = authenticatedCartUtils;
    }


    public void addToCart(Authentication connectedUser, Long productId) {
        Cart cart = cartValidator.validateCartAndUser(connectedUser);
        Product product = productValidator.validateProductAndFindById(productId);
        authenticatedCartUtils.addOrUpdateCartItem(cart, product);
        cartRepository.save(cart);
    }

    public CartDto getCart(Authentication connectedUser) {
        roleValidator.verifyUserRole(connectedUser);
        Cart cart = cartValidator.validateCartAndUser(connectedUser);
        return CartMapper.INSTANCE.toCartDto(cart);
    }

    public void decreaseProductQuantityForAuthenticatedUser(Long productId, Authentication connectedUser) {
        Cart cart = cartValidator.validateCartAndUser(connectedUser);
        CartItem cartItem = cartItemRepository.findByProductIdAndCartId(productId, cart.getId());
        if (cartItem == null) {
            throw new CartItemNotFoundException("Product not found in Cart");
        }
        int newQuantity = cartItem.getQuantity() - 1;
        if (newQuantity <= 0) {
            // If the new quantity equal or less than 0 , remove the product from cart
            cartItemRepository.delete(cartItem);
        } else {
            cartItem.setQuantity(newQuantity);
            cartItemRepository.save(cartItem);
        }
    }

    public void increaseQuantityForAuthenticated(Authentication connectedUser, Long productId) {
        Cart cart = cartValidator.validateCartAndUser(connectedUser);
        CartItem cartItem = cartItemRepository.findByProductIdAndCartId(productId, cart.getId());
        if (cartItem == null) {
            throw new CartItemNotFoundException("Product with id " + productId + " not found in Cart");
        }
        int newQuantity = cartItem.getQuantity() + 1;
        if (stockUtils.isStockAvailable(productId, newQuantity)) {
            cartItem.setQuantity(newQuantity);
            cartItemRepository.save(cartItem);
        } else {
            throw new InsufficientStockException("Insufficient stock for product: " + productId);
        }
    }

    public void removeAllCartItemFromCart(Authentication connectedUser) {
        Cart cart = cartValidator.validateCartAndUser(connectedUser);
        if (cart != null) {
            cart.getItems().clear();
            cartRepository.save(cart);
        } else {
            throw new EmptyCartException("There is no authenticated cart to delete");
        }
    }

    public void removeCartItemFromCart(Long productId, Authentication connectedUser) {
        CustomUserDetails userDetails = (CustomUserDetails) connectedUser.getPrincipal();
        User user = userDetails.getUser();
        //Get the user's cart
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new EmptyCartException("There is no cart to remove"));
        if (cart != null) {
            CartItem cartItem = cartItemRepository.findByProductIdAndCartId(productId, cart.getId());
            if (cartItem != null) {
                cartItemRepository.delete(cartItem);
            } else {
                throw new CartItemNotFoundException("Cart Item with id " + productId + " not found in Cart");
            }
        }
    }

    public void removeSelectedCartItemsFromCart(List<Long> productId, Authentication connectedUser) {
        CustomUserDetails userDetails = (CustomUserDetails) connectedUser.getPrincipal();
        User user = userDetails.getUser();
        //Get the user's cart
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new EmptyCartException("There is no cart to remove"));
        if (cart != null) {
            for (Long product : productId) {
                CartItem cartItem = cartItemRepository.findByProductIdAndCartId(product, cart.getId());
                if (cartItem != null) {
                    cartItemRepository.delete(cartItem);
                } else {
                    throw new ProductNotFoundException("Product with id " + product + " does not exist");
                }
            }
        } else {
            throw new CartItemNotFoundException("There is no Cart Item to remove");
        }
    }

    public Boolean isEmptyForAuthenticated(Authentication connectedUser) {
        Cart cart = cartValidator.validateCartAndUser(connectedUser);
        return cart == null || cart.getItems().isEmpty();
    }

}
