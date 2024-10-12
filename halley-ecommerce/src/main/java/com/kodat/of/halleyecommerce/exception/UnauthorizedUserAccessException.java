package com.kodat.of.halleyecommerce.exception;

public class UnauthorizedUserAccessException extends RuntimeException{
    public UnauthorizedUserAccessException(String message) {
        super(message);
    }
}
