package com.kodat.of.halleyecommerce.cart.service.impl;

import com.kodat.of.halleyecommerce.cart.Cart;
import com.kodat.of.halleyecommerce.cart.CartItem;
import com.kodat.of.halleyecommerce.cart.CartItemRepository;
import com.kodat.of.halleyecommerce.cart.CartRepository;
import com.kodat.of.halleyecommerce.dto.cart.AddToCartRequest;
import com.kodat.of.halleyecommerce.dto.cart.CartDto;
import com.kodat.of.halleyecommerce.dto.cart.CartSummaryDto;
import com.kodat.of.halleyecommerce.exception.*;
import com.kodat.of.halleyecommerce.mapper.cart.CartMapper;
import com.kodat.of.halleyecommerce.user.CustomUserDetails;
import com.kodat.of.halleyecommerce.user.User;
import com.kodat.of.halleyecommerce.util.CartManagerUtils;
import com.kodat.of.halleyecommerce.validator.CartValidator;
import com.kodat.of.halleyecommerce.validator.RoleValidator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
@Service
public class CartManagerAuthenticatedService {
    private final CartRepository cartRepository;
    private final RoleValidator roleValidator;
    private final CartValidator cartValidator;
    private final CartItemRepository cartItemRepository;
    private final CartManagerUtils cartManagerUtils;

    public CartManagerAuthenticatedService(CartRepository cartRepository, RoleValidator roleValidator, CartValidator cartValidator, CartItemRepository cartItemRepository, CartManagerUtils cartManagerUtils) {
        this.cartRepository = cartRepository;
        this.roleValidator = roleValidator;
        this.cartValidator = cartValidator;
        this.cartItemRepository = cartItemRepository;
        this.cartManagerUtils = cartManagerUtils;
    }


    public CartDto addToCartForAuthenticatedUser(AddToCartRequest request, Authentication connectedUser) {
        CustomUserDetails customUserDetails = (CustomUserDetails) connectedUser.getPrincipal();
        User user = customUserDetails.getUser();
        if (user == null) {
            throw new UserNotFoundException("User can not be found");
        }
        roleValidator.verifyUserRole(connectedUser);
        Cart cart = cartValidator.validateCartAndUser(connectedUser);
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user); // Set the user
            cart = cartRepository.save(cart); // Save the new cart
        }
        return cartManagerUtils.addProduct(CartMapper.INSTANCE.toCartDto(cart), request.getProductId(), request.getQuantity());
    }

    public CartDto decreaseProductQuantityForAuthenticatedUser(Long productId, Integer quantity, Authentication connectedUser) {
        Cart cart = cartValidator.validateCartAndUser(connectedUser);
        CartItem cartItem = cartItemRepository.findByProductIdAndCartId(productId, cart.getId());
        if (cartItem == null) {
            throw new CartItemNotFoundException("Product not found in Cart");
        }
        int newQuantity = cartItem.getQuantity() - quantity;
        if (newQuantity <= 0) {
            // If the new quantity equal or less than 0 , remove the product from cart
            cartItemRepository.delete(cartItem);
            return CartMapper.INSTANCE.toCartDto(cart);
        }
        cartItem.setQuantity(newQuantity);
        cartItemRepository.save(cartItem);
        return CartMapper.INSTANCE.toCartDto(cart);
    }
    public void clearAuthenticatedCart(Authentication connectedUser) {
        Cart cart = cartValidator.validateCartAndUser(connectedUser);
        if (cart != null) {
            cartItemRepository.deleteAllByCartId(cart.getId());
        }
        else {
            throw new EmptyCartException("There is no authenticated cart to delete");
        }
    }
    public CartSummaryDto getCartSummaryAuthenticated(Authentication connectedUser) {
        Cart cart = cartValidator.validateCartAndUser(connectedUser);
        if (cart == null) {
            throw new EmptyCartException("Cart does not exist: There is no cart to authenticate");
        }
        BigDecimal totalPrice = cart.getItems().stream()
                .map(item -> item.getProduct().getDiscountedPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        int totalQuantity = cart.getItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
        return CartSummaryDto.builder()
                .cartItems(CartMapper.INSTANCE.toCartItemDtoList(cart.getItems()))
                .totalPrice(totalPrice)
                .totalItems(totalQuantity)
                .build();
    }
    public Boolean isEmptyForAuthenticated(Authentication connectedUser) {
        Cart cart = cartValidator.validateCartAndUser(connectedUser);
        return cart == null || cart.getItems().isEmpty();
    }

    public CartDto getCart(Authentication connectedUser) {
        roleValidator.verifyUserRole(connectedUser);
        Cart cart = cartValidator.validateCartAndUser(connectedUser);
        return CartMapper.INSTANCE.toCartDto(cart);
    }

    public CartDto removeFromAuthenticatedUser(Long productId, Authentication connectedUser) {
        CustomUserDetails userDetails = (CustomUserDetails) connectedUser.getPrincipal();
        User user = userDetails.getUser();
        //Get the user's cart
        Cart cart = cartRepository.findByUser(user);
        if (cart != null) {
            CartItem cartItem = cartItemRepository.findByProductIdAndCartId(productId, cart.getId());
            if (cartItem != null) {
                cartItemRepository.delete(cartItem);
            } else {
                throw new CartNotFoundException("Cart does not exist: There is no cart to remove");
            }
        }
        return CartMapper.INSTANCE.toCartDto(cart);
    }

    public CartDto updateQuantitiesForAuthenticated(Authentication connectedUser, Map<Long,Integer> productQuantities) {
        Cart cart = cartValidator.validateCartAndUser(connectedUser);
        if (cart == null) {
            throw new CartNotFoundException("Cart does not exist: There is no cart to update");
        }
        AtomicBoolean isUpdated = new AtomicBoolean(false);
        cart.getItems().forEach(item -> {
            Long productId = item.getProduct().getId();
            if (productQuantities.containsKey(productId)) {
                Integer newQuantity = productQuantities.get(productId);
                if (cartManagerUtils.isQuantityUpdated(newQuantity, item.getQuantity())) {
                    item.setQuantity(newQuantity);
                    isUpdated.set(true);
                }
            }
        });
        if (isUpdated.get()){
            cartRepository.save(cart);
        }
        return CartMapper.INSTANCE.toCartDto(cart);
    }


}
