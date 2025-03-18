//package com.kodat.of.halleyecommerce.security;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.NonNull;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.util.UUID;
//
//@Component
//public class CookieFilter extends OncePerRequestFilter {
//
//    private static final String CART_TOKEN_COOKIE_NAME = "cartToken";
//
//    @Override
//    protected  void doFilterInternal(
//            @NonNull HttpServletRequest request,
//            @NonNull HttpServletResponse response,
//            @NonNull FilterChain filterChain) {
//        Cookie[] cookies = request.getCookies();
//        boolean hasCartToken = false;
//
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if (CART_TOKEN_COOKIE_NAME.equals(cookie.getName())) {
//                    hasCartToken = true;
//                    break;
//                }
//            }
//        }
//
//        if (!hasCartToken) {
//            String cartToken = UUID.randomUUID().toString();
//            Cookie cartTokenCookie = new Cookie(CART_TOKEN_COOKIE_NAME, cartToken);
//            cartTokenCookie.setHttpOnly(true);
//            cartTokenCookie.setSecure(false);
//            cartTokenCookie.setPath("/");
//            cartTokenCookie.setMaxAge(60 * 60 * 24 * 30);
//            response.addCookie(cartTokenCookie);
//        }
//
//
//        try {
//            filterChain.doFilter(request, response);
//        } catch (Exception e) {
//            throw new RuntimeException("Filter error", e);
//        }
//    }
//}
