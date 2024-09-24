package com.kodat.of.halleyecommerce.exception;

public class UnauthorizedAdminAccessException extends RuntimeException {
    public UnauthorizedAdminAccessException(String message) {
        super(message);
    }
}
