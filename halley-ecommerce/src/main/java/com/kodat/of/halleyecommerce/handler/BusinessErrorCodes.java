package com.kodat.of.halleyecommerce.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BusinessErrorCodes {
    VALIDATION_ERROR(400, HttpStatus.BAD_REQUEST, "Validation error"),
    UNAUTHORIZED_ADMIN_ACCESS(403,HttpStatus.FORBIDDEN,"Unauthorized access attempt by non-admin user"),
    UNAUTHORIZED_USER_ACCESS(403,HttpStatus.FORBIDDEN,"Unauthorized access attempt by non-user"),
    PRODUCT_NOT_FOUND(404, HttpStatus.NOT_FOUND, "Product not found"),
    ENTITY_NOT_FOUND(404, HttpStatus.NOT_FOUND, "Entity not found"),
    USER_NOT_FOUND(404, HttpStatus.NOT_FOUND, "User not found"),
    PARENT_CATEGORY_DOES_NOT_EXISTS(404,HttpStatus.NOT_FOUND,"Parent category does not exists."),
    CATEGORY_DOES_NOT_EXISTS(404,HttpStatus.NOT_FOUND,"Category with this id not found."),
    DISCOUNT_NOT_FOUND(404, HttpStatus.NOT_FOUND, "Discount not found"),
    ADDRESS_NOT_FOUND(404, HttpStatus.NOT_FOUND, "Address not found"),
    EMPTY_CART(404, HttpStatus.NOT_FOUND, "Empty cart"),
    CART_NOT_FOUND(404, HttpStatus.NOT_FOUND, "Cart not found"),
    CART_ITEM_NOT_FOUND(404, HttpStatus.NOT_FOUND, "Cart item not found"),
    CATEGORY_ALREADY_EXISTS(409, HttpStatus.CONFLICT, "Category already exists"),
    DATA_INTEGRITY_VIOLATION(409, HttpStatus.CONFLICT, "Data integrity violation"),
    USER_ALREADY_EXISTS(409, HttpStatus.CONFLICT, "User already exist"),
    PRODUCT_ALREADY_EXISTS(409,HttpStatus.CONFLICT,"Product already exists."),
    INVALID_PARENT_CATEGORY_EXCEPTION(409,HttpStatus.CONFLICT,"A parent category cannot have another parent."),
    PARENT_CATEGORY_CYCLE_EXCEPTION(409,HttpStatus.CONFLICT,"A category cannot be assigned as a parent of its own child category."),
    INVALID_DISCOUNT_EXCEPTION(409,HttpStatus.CONFLICT,"Invalid discount percentage."),
    ADDRESS_ALREADY_EXISTS_EXCEPTION(409,HttpStatus.CONFLICT,"Address already exists"),
    INSUFFICIENT_STOCK(409 , HttpStatus.CONFLICT,"Insufficient stock")
            {
    };


    private final int code;
    private final HttpStatus httpStatus;
    private final String description;


    BusinessErrorCodes(int code, HttpStatus httpStatus, String description) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.description = description;
    }


}
