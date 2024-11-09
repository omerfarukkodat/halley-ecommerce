package com.kodat.of.halleyecommerce.cart;

import com.kodat.of.halleyecommerce.cart.service.CartService;
import com.kodat.of.halleyecommerce.dto.cart.CartDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<CartDto> getCart(Authentication connectedUser) {
        return ResponseEntity.ok(cartService.getCart(connectedUser));
    }

    @PostMapping("/{productId}")
    public ResponseEntity<Void> addToCart(Authentication connectedUser, @PathVariable Long productId) {
        cartService.addToCart(connectedUser, productId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> removeAllCartItemFromCart(Authentication connectedUser) {
        cartService.removeAllCartItemFromCart(connectedUser);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> removeCartItemFromCart(
            Authentication connectedUser,
            @PathVariable Long productId) {
        cartService.removeCartItemFromCart(connectedUser, productId);
        return ResponseEntity.status(HttpStatus.OK).body("Successfully removed cart items from cart");
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeSelectedCartItemsFromCart(
            @RequestBody List<Long> productIds,
            Authentication connectedUser) {
        cartService.removeSelectedCartItemsFromCart(productIds, connectedUser);
        return ResponseEntity.status(HttpStatus.OK).body("Cart items removed from Cart.");
    }

    @PatchMapping("/{productId}/decrease")
    public ResponseEntity<Void> decreaseProductQuantity(
            Authentication connectedUser,
            @PathVariable Long productId) {
        cartService.decreaseProductQuantity(connectedUser, productId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{productId}/increase")
    public ResponseEntity<Void> increaseProductQuantity(
            @PathVariable Long productId,
            Authentication connectedUser) {
        cartService.increaseProductQuantity(connectedUser,productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/isEmpty")
    public ResponseEntity<Boolean> isEmptyCart(Authentication connectedUser) {
        return ResponseEntity.ok(cartService.isEmptyCart(connectedUser));
    }


}

