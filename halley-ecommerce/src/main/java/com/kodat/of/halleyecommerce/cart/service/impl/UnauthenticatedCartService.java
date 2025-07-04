package com.kodat.of.halleyecommerce.cart.service.impl;

import com.kodat.of.halleyecommerce.cart.Cart;
import com.kodat.of.halleyecommerce.cart.CartItem;
import com.kodat.of.halleyecommerce.cart.CartItemRepository;
import com.kodat.of.halleyecommerce.cart.CartRepository;
import com.kodat.of.halleyecommerce.dto.cart.CartDto;
import com.kodat.of.halleyecommerce.dto.cart.CartItemDto;
import com.kodat.of.halleyecommerce.dto.cart.CartSummaryDto;
import com.kodat.of.halleyecommerce.exception.CartItemNotFoundException;
import com.kodat.of.halleyecommerce.exception.CartNotFoundException;
import com.kodat.of.halleyecommerce.mapper.cart.CartMapper;
import com.kodat.of.halleyecommerce.product.Product;
import com.kodat.of.halleyecommerce.util.UnauthenticatedUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UnauthenticatedCartService {

    private final UnauthenticatedUtils unauthenticatedUtils;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;



    // Add a product to the cart
    public void addToCart(Long productId) {
        Cart cart = unauthenticatedUtils.getOrCreateCart();
        unauthenticatedUtils.addProduct(cart, productId);
    }

    public CartDto getCart() {
        Cart cart = unauthenticatedUtils.getOrCreateCart();
        if (!cart.getItems().isEmpty()) {
            List<CartItem> cartItems = new ArrayList<>(cart.getItems());
            List<CartItem> itemsToRemove = new ArrayList<>();

            for (CartItem cartItem : cartItems) {
                Product product = cartItem.getProduct();
                if (product.getStock() == 0) {
                    itemsToRemove.add(cartItem);
                } else if (cartItem.getQuantity() >= product.getStock()) {
                    cartItem.setQuantity(product.getStock());
                    cartItemRepository.save(cartItem);
                }
            }

            for (CartItem item : itemsToRemove) {
                cart.getItems().remove(item);
                cartItemRepository.delete(item);
            }

            cartRepository.save(cart);
        }
        return CartMapper.INSTANCE.toCartDto(cart);
    }

    // Remove a product from the cart
    public void removeCartItemFromCart(Long productId) {
        Cart cart = unauthenticatedUtils.getOrCreateCart();
        if (cart == null) {
            throw new CartNotFoundException("Cart does not exist in the session");
        }
        CartItem cartItem = cartItemRepository.findByProductIdAndCartId(productId,cart.getId());
        if (cartItem != null) {
            cartItemRepository.delete(cartItem);
            cartRepository.save(cart);
        } else {
            throw new CartItemNotFoundException("Product does not exist in session");
        }
    }

    // Decrease product quantity for unauthenticated user
    public void decreaseProductQuantityForUnauthenticatedUser(Long productId) {
        Cart cart = unauthenticatedUtils.getOrCreateCart();
        CartItem cartItem = cartItemRepository.findByProductIdAndCartId(productId, cart.getId());
        if (cartItem == null) {
            throw new CartNotFoundException("Product not found in the cart");
        }
        int newQuantity = cartItem.getQuantity() - 1;
        if (newQuantity <= 0) {
            cartItemRepository.delete(cartItem);
        } else {

            cartItem.setQuantity(newQuantity);
            cartItemRepository.save(cartItem);
        }
    }


    public void increaseQuantityForUnauthenticated(Long productId) {
        Cart cart = unauthenticatedUtils.getOrCreateCart();
        CartDto cartDto = CartMapper.INSTANCE.toCartDto(cart);
        if (cartDto == null) {
            throw new CartNotFoundException("Cart does not exist in session");
        }
        CartItemDto existingItem = unauthenticatedUtils.findItemByProductId(cartDto, productId);
        if (existingItem == null) {
            throw new CartItemNotFoundException("Product does not exist in session");
        }
        Product product = unauthenticatedUtils.fetchProductById(productId);
        unauthenticatedUtils.updateExistingCartItem(cart, existingItem, product.getStock());
        cartDto.setId(cart.getId());
    }

    // Clear the cart
    public void removeAllCartItemFromCart() {
        Cart cart = unauthenticatedUtils.getOrCreateCart();
        CartDto cartDto = CartMapper.INSTANCE.toCartDto(cart);
        if (cartDto == null) {
            throw new CartNotFoundException("Cart does not exist in session");
        }
        if (cartDto.getItems().isEmpty()) {
            throw new CartNotFoundException("Cart is already empty");
        } else {
            cart.getItems().clear();
            cartRepository.save(cart);
            cartDto.getItems().clear();
        }
    }

    // Check if cart is empty for unauthenticated user
    public Boolean isEmptyForUnauthenticated() {
        Cart cart = unauthenticatedUtils.getOrCreateCart();
        CartDto cartDto = CartMapper.INSTANCE.toCartDto(cart);
        return cartDto == null || cartDto.getItems().isEmpty();
    }

    public void removeSelectedCartItemsFromCart(List<Long> productIds) {
        Cart cart = unauthenticatedUtils.getOrCreateCart();
        CartDto cartDto = CartMapper.INSTANCE.toCartDto(cart);
        if (cartDto == null) {
            throw new CartNotFoundException("Cart does not exist in the session");
        }
        for (Long productId : productIds) {
            CartItem cartItem = cartItemRepository.findByProductIdAndCartId(productId, cart.getId());
            if (cartItem != null) {
                cartItemRepository.delete(cartItem);
                cartDto.getItems().removeIf(item -> item.getId().equals(cartItem.getId()));
            } else {
                throw new CartItemNotFoundException("Product with ID " + productId + " does not exist in session");
            }
        }
    }

    public Integer getProductQuantity(Long productId) {
        Cart cart = unauthenticatedUtils.getOrCreateCart();
        CartDto cartDto = CartMapper.INSTANCE.toCartDto(cart);
        if (cartDto == null) {
            throw new CartNotFoundException("Cart does not exist in the session");
        }
        CartItemDto existingItem = unauthenticatedUtils.findItemByProductId(cartDto, productId);
        return existingItem == null ? 0 : existingItem.getQuantity();

    }

    public CartSummaryDto getCartSummary() {
        Cart cart = unauthenticatedUtils.getOrCreateCart();

        BigDecimal productPrices = unauthenticatedUtils.calculateTotalPrice(cart.getItems());
        BigDecimal shippingCoast = unauthenticatedUtils.calculateShippingCoast(productPrices);
        BigDecimal totalPrice = productPrices.add(shippingCoast);
        int totalQuantity = cart.getItems().size();

        return CartSummaryDto.builder()
                .productPrices(productPrices)
                .totalShippingCost(shippingCoast)
                .totalPrice(totalPrice)
                .totalQuantity(totalQuantity)
                .build();
    }
}

