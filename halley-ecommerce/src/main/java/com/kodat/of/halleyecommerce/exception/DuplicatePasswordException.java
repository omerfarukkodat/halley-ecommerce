package com.kodat.of.halleyecommerce.exception;

public class DuplicatePasswordException extends RuntimeException {
    public DuplicatePasswordException(String message) {
        super(message);
    }
}
