package com.kodat.of.halleyecommerce.config;

import com.kodat.of.halleyecommerce.cart.Cart;
import com.kodat.of.halleyecommerce.cart.CartItemRepository;
import com.kodat.of.halleyecommerce.cart.CartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class CartCleanupTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(CartCleanupTask.class);
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public CartCleanupTask(CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void cleanUpExpiredCarts() {
        LOGGER.info("Cleaning up expired carts for non-registered users...");
        // Get expired guestCart (has cartToken)
        List<Cart> expiredCart = cartRepository.findAllByCartTokenIsNotNullAndCreatedAtBefore(LocalDateTime.now());
        if (!expiredCart.isEmpty()) {
            List<String> expiredCartTokens = expiredCart.stream()
                    .map(Cart::getCartToken)
                    .toList();
            LOGGER.info("Found {} expired carts.", expiredCartTokens.size());
            cartItemRepository.deleteAllByCart_CartTokenIn(expiredCartTokens);
            cartRepository.deleteAllByCartTokens(expiredCartTokens);
            LOGGER.info("Deleted expired carts and their items successfully.");
//            for (Cart cart : expiredCart) {
//                LOGGER.info("Deleting cart with token: {}", cart.getCartToken());
//                cartItemRepository.deleteCartItemByCart_CartToken(cart.getCartToken());
//                cartRepository.delete(cart);
//            }
        } else {
            LOGGER.info("No expired carts found");
        }
    }
}

