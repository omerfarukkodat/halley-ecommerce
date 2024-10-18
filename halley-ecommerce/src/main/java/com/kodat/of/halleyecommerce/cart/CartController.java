package com.kodat.of.halleyecommerce.cart;

import com.kodat.of.halleyecommerce.cart.service.CartService;
import com.kodat.of.halleyecommerce.dto.cart.AddToCartRequest;
import com.kodat.of.halleyecommerce.dto.cart.CartDto;
import com.kodat.of.halleyecommerce.dto.cart.CartSummaryDto;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<CartDto> getCart(Authentication connectedUser, HttpSession session) {
        return ResponseEntity.ok(cartService.handleGetCart(connectedUser, session));

    }

    @PostMapping
    public ResponseEntity<CartDto> addToCart(Authentication connectedUser, HttpSession session, @Valid @RequestBody AddToCartRequest request) {
        return ResponseEntity.ok(cartService.addToCart(connectedUser, session, request));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<CartDto> removeFromCart(Authentication connectedUser, HttpSession session,
                                                  @PathVariable Long productId) {
        return ResponseEntity.ok(cartService.removeFromCart(connectedUser, session, productId));
    }

    @PatchMapping("/{productId}/decrease")
    public ResponseEntity<CartDto> decreaseProductQuantity(
            Authentication connectedUser,
            HttpSession session,
            @PathVariable Long productId,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(cartService.decreaseProductQuantity(connectedUser, session, productId, quantity));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(Authentication connectedUser, HttpSession session) {
        cartService.clearCart(connectedUser,session);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/summary")
    public ResponseEntity<CartSummaryDto> getCartSummary(Authentication connectedUser, HttpSession session) {
        return ResponseEntity.ok(cartService.getCartSummary(connectedUser,session));
    }
    @GetMapping("/isEmpty")
    public ResponseEntity<Boolean> isEmptyCart(Authentication connectedUser , HttpSession session) {
        return ResponseEntity.ok(cartService.isEmptyCart(connectedUser,session));
    }
    @PatchMapping("/updateQuantities")
    public ResponseEntity<CartDto> updateAllQuantities(
            Authentication connectedUser,
            HttpSession session,
            @RequestBody Map <Long , Integer> productQuantities) {
        return ResponseEntity.ok(cartService.updateAllQuantities(connectedUser,session , productQuantities));
    }



}

