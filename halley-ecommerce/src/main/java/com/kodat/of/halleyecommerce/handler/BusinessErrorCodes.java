package com.kodat.of.halleyecommerce.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum BusinessErrorCodes {


    PRODUCT_NOT_FOUND(404, HttpStatus.NOT_FOUND, "Product Not Found"),
    ENTITY_NOT_FOUND(404, HttpStatus.NOT_FOUND, "Entity not found"),
    USER_NOT_FOUND(404, HttpStatus.NOT_FOUND, "User not found"),
    CATEGORY_ALREADY_EXISTS(409, HttpStatus.CONFLICT, "Category already exists"),
    DATA_INTEGRITY_VIOLATION(409, HttpStatus.CONFLICT, "Data Integrity Violation"),
    VALIDATION_ERROR(400, HttpStatus.BAD_REQUEST, "Validation Error"),
    USER_ALREADY_EXISTS(409, HttpStatus.CONFLICT, "User already Exists"),
    UNAUTHORIZED_ADMIN_ACCESS(403,HttpStatus.FORBIDDEN,"Unauthorized access attempt by non-admin user")
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
