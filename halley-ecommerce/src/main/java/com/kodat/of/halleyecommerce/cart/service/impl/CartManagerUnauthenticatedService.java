package com.kodat.of.halleyecommerce.cart.service.impl;

import com.kodat.of.halleyecommerce.cart.CartItem;
import com.kodat.of.halleyecommerce.dto.cart.AddToCartRequest;
import com.kodat.of.halleyecommerce.dto.cart.CartDto;
import com.kodat.of.halleyecommerce.dto.cart.CartItemDto;
import com.kodat.of.halleyecommerce.dto.cart.CartSummaryDto;
import com.kodat.of.halleyecommerce.exception.CartItemNotFoundException;
import com.kodat.of.halleyecommerce.exception.CartNotFoundException;
import com.kodat.of.halleyecommerce.exception.EmptyCartException;
import com.kodat.of.halleyecommerce.mapper.cart.CartMapper;
import com.kodat.of.halleyecommerce.util.CartManagerUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class CartManagerUnauthenticatedService {

    private final CartManagerUtils cartManagerUtils;

    public CartManagerUnauthenticatedService(CartManagerUtils cartManagerUtils) {
        this.cartManagerUtils = cartManagerUtils;
    }

    public CartDto addToCartForUnauthenticatedUser(AddToCartRequest request, HttpSession session) {
        CartDto sessionCart = (CartDto) session.getAttribute("cart");
        if (sessionCart == null) {
            sessionCart = new CartDto();
        }
        CartDto updatedCartDto = cartManagerUtils.addProduct(sessionCart, request.getProductId(), request.getQuantity());
        session.setAttribute("cart", updatedCartDto);
        return updatedCartDto;
    }

    public CartDto removeFromUnAuthenticatedUser(Long productId, HttpSession session) {
        CartDto sessionCart = (CartDto) session.getAttribute("cart");
        if (sessionCart == null) {
            throw new CartNotFoundException("Cart does not exist in the session");
        }
        CartItemDto existingItem = cartManagerUtils.findItemByProductId(sessionCart, productId);
        if (existingItem != null) {
            sessionCart.getItems().remove(existingItem);
            session.setAttribute("cart", sessionCart);
        } else {
            throw new CartItemNotFoundException("Product does not exist in session");
        }
        return sessionCart;
    }

    public CartDto decreaseProductQuantityForUnauthenticatedUser(Long productId, Integer quantity, HttpSession session) {
        CartDto sessionCart = (CartDto) session.getAttribute("cart");
        if (sessionCart == null) {
            throw new CartNotFoundException("Cart does not exist in session");
        }
        CartItemDto cartItemDto = cartManagerUtils.findItemByProductId(sessionCart, productId);
        if (cartItemDto == null) {
            throw new CartItemNotFoundException("Product does not exist in session");
        }
        int newQuantity = cartItemDto.getQuantity() - quantity;
        if (newQuantity <= 0) {
            sessionCart.getItems().remove(cartItemDto);
            session.setAttribute("cart", sessionCart);
            return sessionCart;
        }
        cartItemDto.setQuantity(newQuantity);
        session.setAttribute("cart", sessionCart);
        return sessionCart;
    }

    public void clearUnauthenticatedCart(HttpSession session) {
        CartDto sessionCart = (CartDto) session.getAttribute("cart");
        if (sessionCart == null) {
            throw new CartNotFoundException("Cart does not exist in session");
        }
        if (sessionCart.getItems().isEmpty()) {
            throw new CartNotFoundException("Cart is already empty");
        }
        sessionCart.getItems().clear();
        session.setAttribute("cart", sessionCart);
    }

    public CartSummaryDto getCartSummaryUnauthenticated(HttpSession session) {
        Object cartItemsObject = session.getAttribute("cartItems");
        if (cartItemsObject instanceof List<?> cartItemsList) {
            if (!cartItemsList.isEmpty() && cartItemsList.getFirst() instanceof CartItem) {
                @SuppressWarnings("unchecked")
                List<CartItem> cartItems = (List<CartItem>) cartItemsList;

                if (cartItems.isEmpty()) {
                throw new EmptyCartException("Cart is empty");
                }
                BigDecimal totalPrice = cartItems.stream()
                        .map(item -> item.getProduct().getDiscountedPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                int totalQuantity = cartItems.stream()
                        .mapToInt(CartItem::getQuantity)
                        .sum();

                return CartSummaryDto.builder()
                        .cartItems(CartMapper.INSTANCE.toCartItemDtoList(cartItems))
                        .totalPrice(totalPrice)
                        .totalItems(totalQuantity)
                        .build();
            }
        }
        throw new EmptyCartException("Cart is empty");
    }


    public Boolean isEmptyForUnauthenticated(HttpSession session) {
        CartDto sessionCart = (CartDto) session.getAttribute("cart");
        return sessionCart == null || sessionCart.getItems().isEmpty();
    }

    public CartDto updateQuantitiesForUnauthenticated(HttpSession session, Map<Long, Integer> productQuantities) {
        Object sessionCartObject = session.getAttribute("cart");
        if (!(sessionCartObject instanceof CartDto sessionCart) || sessionCart.getItems() == null) {
            throw new CartNotFoundException("Cart does not exist in session");
        }
        AtomicBoolean isUpdated = new AtomicBoolean(false);
        sessionCart.getItems().forEach(item -> {
            Long productId = item.getProduct().getId();
            if (productQuantities.containsKey(productId)) {
                Integer newQuantity = productQuantities.get(productId);
                if (cartManagerUtils.isQuantityUpdated(newQuantity, item.getQuantity())) {
                    isUpdated.set(true);
                    item.setQuantity(newQuantity);
                }
            }
        });
        if (isUpdated.get()){
            session.setAttribute("cart", sessionCart);
        }
        return sessionCart;
    }
}
