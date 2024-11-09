package com.kodat.of.halleyecommerce.util;

import com.kodat.of.halleyecommerce.dto.cart.CartDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {
    private static final String CART_PREFIX = "cart:";
    private static final long CART_EXPIRATION_DAYS = 3;

    private final RedisTemplate<String, CartDto> redisTemplate;

    public RedisUtils(RedisTemplate<String, CartDto> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveCartToRedis(String cartToken, CartDto cartDto) {
        redisTemplate.opsForValue().set(CART_PREFIX + cartToken, cartDto, CART_EXPIRATION_DAYS, TimeUnit.DAYS);
    }

    public CartDto getCartFromRedis(String cartToken) {
        return redisTemplate.opsForValue().get(CART_PREFIX + cartToken);
    }

    public void clearCartFromRedis(String cartToken) {
        redisTemplate.delete(CART_PREFIX + cartToken);
    }
}
