package com.kodat.of.halleyecommerce.exception;

public class ParentCategoryCycleException extends RuntimeException {
    public ParentCategoryCycleException(String message) {
        super(message);
    }
}
