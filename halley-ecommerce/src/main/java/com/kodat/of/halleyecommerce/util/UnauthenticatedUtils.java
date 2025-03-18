package com.kodat.of.halleyecommerce.util;

import com.kodat.of.halleyecommerce.cart.Cart;
import com.kodat.of.halleyecommerce.cart.CartItem;
import com.kodat.of.halleyecommerce.cart.CartItemRepository;
import com.kodat.of.halleyecommerce.cart.CartRepository;
import com.kodat.of.halleyecommerce.dto.cart.CartDto;
import com.kodat.of.halleyecommerce.dto.cart.CartItemDto;
import com.kodat.of.halleyecommerce.exception.InsufficientStockException;
import com.kodat.of.halleyecommerce.exception.ProductNotFoundException;
import com.kodat.of.halleyecommerce.mapper.cart.CartMapper;
import com.kodat.of.halleyecommerce.product.Product;
import com.kodat.of.halleyecommerce.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Component
public class UnauthenticatedUtils {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CookieUtils cookieUtils;
    private final ShippingUtils shippingUtils;


    @Transactional
    public void addProduct(Cart cart, Long productId) {
        Product product = fetchProductById(productId);
        if (product.getStock() <= 0) {
            throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
        }
        CartDto cartDto = CartMapper.INSTANCE.toCartDto(cart);
        CartItemDto existingItem = findItemByProductId(cartDto, productId);
        if (existingItem != null) {
            updateExistingCartItem(cart, existingItem, product.getStock());
        } else {
            addNewCartItem(cart, cartDto, product);
        }

        cartDto.setId(cart.getId());
    }

    public Product fetchProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    private void validateQuantity(int quantity, int availableStock) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        if (quantity > availableStock) {
            throw new InsufficientStockException("Insufficient stock available. Only " + availableStock + " left");
        }
    }

    public synchronized Cart getOrCreateCart() {
        String cartToken = cookieUtils.getOrCreateCartToken();
        return cartRepository.findByCartToken(cartToken)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setCartToken(cartToken);
                    return cartRepository.save(cart);
                });
    }

    public void updateExistingCartItem(Cart cart, CartItemDto existingItem, int availableStock) {
        int newQuantity = existingItem.getQuantity() + 1;
        validateQuantity(newQuantity, availableStock);

        existingItem.setQuantity(newQuantity);
        updateCartItem(cart, existingItem);
    }

    public void updateCartItem(Cart cart, CartItemDto existingItem) {
        CartItem cartItem = cartItemRepository.findByProductIdAndCartId(existingItem.getProduct().getId(), cart.getId());
        if (cartItem != null) {
            cartItem.setQuantity(existingItem.getQuantity());
            cartItemRepository.save(cartItem);
        }
    }

    private void addNewCartItem(Cart cart, CartDto cartDto, Product product) {
        CartItem cartItem = CartItem.builder()
                .product(product)
                .quantity(1)
                .cart(cart)
                .build();
        cartItemRepository.save(cartItem);
        cartDto.getItems().add(CartMapper.INSTANCE.toCartItemDto(cartItem));
    }

    public CartItemDto findItemByProductId(CartDto cartDto, Long productId) {
        return cartDto.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    public BigDecimal calculateTotalPrice(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(item -> item.getProduct().getDiscountedPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateShippingCoast(BigDecimal totalPrice) {
        return shippingUtils.calculateShippingCost(totalPrice);
    }
}
