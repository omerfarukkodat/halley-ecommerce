package com.kodat.of.halleyecommerce.config;

import com.kodat.of.halleyecommerce.cart.Cart;
import com.kodat.of.halleyecommerce.cart.CartItemRepository;
import com.kodat.of.halleyecommerce.cart.CartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class KeyExpirationListener implements MessageListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(KeyExpirationListener.class);
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public KeyExpirationListener(CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }
    @Transactional
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel());
        String key = new String(message.getBody());

        LOGGER.info("Received message: Channel = {}, Key = {}", channel, key);
        if (key.startsWith("cart:")){
            String cartToken = key.replace("cart:", "");
            LOGGER.info("Deleting cart with token: {}", cartToken);
            Optional<Cart> cart = cartRepository.findByCartToken(cartToken);
            if (cart.isPresent()){
                cartItemRepository.deleteCartItemByCart_CartToken(cart.get().getCartToken());
                cartRepository.delete(cart.get());
            }else {
                LOGGER.warn("No cart found with token {}" , cartToken);
            }
        }
    }
    }
