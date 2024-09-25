package com.kodat.of.halleyecommerce.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum BusinessErrorCodes {


    PRODUCT_NOT_FOUND(404, HttpStatus.NOT_FOUND, "Product not found"),
    ENTITY_NOT_FOUND(404, HttpStatus.NOT_FOUND, "Entity not found"),
    USER_NOT_FOUND(404, HttpStatus.NOT_FOUND, "User not found"),
    CATEGORY_ALREADY_EXISTS(409, HttpStatus.CONFLICT, "Category already exists"),
    DATA_INTEGRITY_VIOLATION(409, HttpStatus.CONFLICT, "Data integrity violation"),
    VALIDATION_ERROR(400, HttpStatus.BAD_REQUEST, "Validation error"),
    USER_ALREADY_EXISTS(409, HttpStatus.CONFLICT, "User already exist"),
    UNAUTHORIZED_ADMIN_ACCESS(403,HttpStatus.FORBIDDEN,"Unauthorized access attempt by non-admin user"),
    PARENT_CATEGORY_DOES_NOT_EXISTS(404,HttpStatus.NOT_FOUND,"Parent category does not exists."),
    PRODUCT_ALREADY_EXISTS(409,HttpStatus.CONFLICT,"Product already exists.")
            {
    };


    @Getter
    private final int code;
    @Getter
    private final HttpStatus httpStatus;
    @Getter
    private final String description;


    BusinessErrorCodes(int code, HttpStatus httpStatus, String description) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.description = description;
    }


}
