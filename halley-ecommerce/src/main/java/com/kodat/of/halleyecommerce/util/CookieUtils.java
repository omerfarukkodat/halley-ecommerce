package com.kodat.of.halleyecommerce.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class CookieUtils {
    private static final String CART_TOKEN_COOKIE_NAME = "cartToken";
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    public CookieUtils(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public String getOrCreateCartToken() {
        Cookie[] cookies = request.getCookies();
        String cartToken = getCookieValue(cookies);

        if (cartToken == null) {
            cartToken = UUID.randomUUID().toString();
            createCartTokenCookie(cartToken);
        }

        return cartToken;
    }

    private String getCookieValue(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (CART_TOKEN_COOKIE_NAME.equals(cookie.getName())) {
                    return cookie.getValue();
                }
                System.out.println(cookie.getValue());
            }
        }
        return null;
    }

    private void createCartTokenCookie(String cartToken) {
        Cookie cookie = new Cookie(CART_TOKEN_COOKIE_NAME, cartToken);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(60 * 60 * 24 * 3); // 3 gün
        response.addCookie(cookie);
    }
}
