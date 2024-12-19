package com.kodat.of.halleyecommerce.exception;

public class RateLimiterAttemptException extends RuntimeException {
    public RateLimiterAttemptException(String message) {
        super(message);
    }
}
