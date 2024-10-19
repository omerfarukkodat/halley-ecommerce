package com.kodat.of.halleyecommerce.util;

import com.kodat.of.halleyecommerce.cart.Cart;
import com.kodat.of.halleyecommerce.cart.CartItem;
import com.kodat.of.halleyecommerce.cart.CartItemRepository;
import com.kodat.of.halleyecommerce.cart.CartRepository;
import com.kodat.of.halleyecommerce.dto.cart.CartDto;
import com.kodat.of.halleyecommerce.dto.cart.CartItemDto;
import com.kodat.of.halleyecommerce.exception.CartItemNotFoundException;
import com.kodat.of.halleyecommerce.exception.InsufficientStockException;
import com.kodat.of.halleyecommerce.exception.ProductNotFoundException;
import com.kodat.of.halleyecommerce.mapper.cart.CartMapper;
import com.kodat.of.halleyecommerce.product.Product;
import com.kodat.of.halleyecommerce.product.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartManagerUtils {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public CartManagerUtils(ProductRepository productRepository, CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }
    public CartDto mergeCarts(CartDto cartDto, List<CartItemDto> sessionCartItems) {
        for (CartItemDto sessionCartItem : sessionCartItems) {
            Product product = productRepository.findById(sessionCartItem.getProduct().getId())
                    .orElseThrow(() -> new CartItemNotFoundException("Product not found"));

            CartItem existingItem = findCartItemInDto(cartDto, product);
            if (existingItem != null) {
                existingItem.setQuantity(sessionCartItem.getQuantity() + existingItem.getQuantity());
            } else {
                cartDto.getItems().add(sessionCartItem);
            }
        }
        Cart mergedCart = cartRepository.save(CartMapper.INSTANCE.toCart(cartDto));
        return CartMapper.INSTANCE.toCartDto(mergedCart);
    }

    public CartDto addProduct(CartDto cartDto, Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        // Quantity control
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity cannot be less than or equal to 0");
        }
        if (quantity > product.getStock()) {
            throw new InsufficientStockException("Insufficient stock available. Only " + product.getStock() + " left.");
        }
        // Check if the cart has the same product
        CartItemDto existingItem = findItemByProductId(cartDto, productId);
        if (existingItem != null) {
            // If the exists , update to quantity
            int newQuantity = existingItem.getQuantity() + quantity;
            if (newQuantity > product.getStock()) {
                throw new InsufficientStockException("Insufficient stock available. Only " + product.getStock() + " left.");
            }
            existingItem.setQuantity(newQuantity);
            // Update the cart items , and save the repo
            CartItem cartItem = cartItemRepository.findByProductIdAndCartId(productId, CartMapper.INSTANCE.toCart(cartDto).getId());
            if (cartItem != null) {
                cartItem.setQuantity(newQuantity);
                cartItemRepository.save(cartItem);  // update existing cart
            }
        } else {
            // If not exist create new one
            CartItemDto newItem = new CartItemDto();
            newItem.setProduct(CartMapper.INSTANCE.toProductResponseDto(product));
            newItem.setQuantity(quantity);
            cartDto.getItems().add(newItem);

            // save the new cart item to the repo
            CartItem cartItem = new CartItem();
            cartItem.setCart(CartMapper.INSTANCE.toCart(cartDto));
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }
        return cartDto;
    }
    public boolean isQuantityUpdated(Integer newQuantity, Integer currentQuantity) {
        return newQuantity != null && newQuantity > 0 && !newQuantity.equals(currentQuantity);
    }

    private CartItem findCartItemInDto(CartDto cartDto, Product product) {
        return cartDto.getItems().stream()
                .map(CartMapper.INSTANCE::toCartItem)
                .filter(cartItem -> cartItem.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);
    }

    public CartItemDto findItemByProductId(CartDto cartDto, Long productId) {
        return cartDto.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
    }
}
