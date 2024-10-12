package com.kodat.of.halleyecommerce.handler;

import com.kodat.of.halleyecommerce.exception.*;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        LOGGER.error("Entity not found: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ExceptionResponse
                                .builder()
                                .businessErrorCode(BusinessErrorCodes.ENTITY_NOT_FOUND.getCode())
                                .businessErrorDescription(BusinessErrorCodes.ENTITY_NOT_FOUND.getDescription())
                                .error(e.getMessage())
                                .build()
                );
    }
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        LOGGER.warn(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.USER_ALREADY_EXISTS.getCode())
                                .businessErrorDescription(BusinessErrorCodes.USER_ALREADY_EXISTS.getDescription())
                                .error(e.getMessage())
                                .build()
                );
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUserNotFoundException(UserNotFoundException e) {
        LOGGER.warn("User does not exist: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.USER_NOT_FOUND.getCode())
                                .businessErrorDescription(BusinessErrorCodes.USER_NOT_FOUND.getDescription())
                                .error(e.getMessage())
                                .build()
                );
    }
    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleCategoryAlreadyExistsException(CategoryAlreadyExistsException e) {
        LOGGER.warn("Category already exists: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.CATEGORY_ALREADY_EXISTS.getCode())
                                .businessErrorDescription(BusinessErrorCodes.CATEGORY_ALREADY_EXISTS.getDescription())
                                .error(e.getMessage())
                                .build()
                );
    }
    @ExceptionHandler(UnauthorizedAdminAccessException.class)
    public ResponseEntity<ExceptionResponse> handleUnauthorizedAdminAccessException(UnauthorizedAdminAccessException e) {
        LOGGER.warn("Unauthorized admin access: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.UNAUTHORIZED_ADMIN_ACCESS.getCode())
                                .businessErrorDescription(BusinessErrorCodes.UNAUTHORIZED_ADMIN_ACCESS.getDescription())
                                .error(e.getMessage())
                                .build()
                );
    }
    @ExceptionHandler(ParentCategoryDoesNotExistsException.class)
    public ResponseEntity<ExceptionResponse> handleParentCategoryDoesNotExistsException(ParentCategoryDoesNotExistsException e) {
        LOGGER.warn("Parent category does not exists: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.PARENT_CATEGORY_DOES_NOT_EXISTS.getCode())
                                .businessErrorDescription(BusinessErrorCodes.PARENT_CATEGORY_DOES_NOT_EXISTS.getDescription())
                                .error(e.getMessage())
                                .build()
                );
    }
    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleProductAlreadyExistsException(ProductAlreadyExistsException e) {
        LOGGER.warn("Product already exists: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.PRODUCT_ALREADY_EXISTS.getCode())
                                .businessErrorDescription(BusinessErrorCodes.PRODUCT_ALREADY_EXISTS.getDescription())
                                .error(e.getMessage())
                                .build()
                );
    }
    @ExceptionHandler(CategoryDoesNotExistsException.class)
    public ResponseEntity<ExceptionResponse> handleCategoryDoesNotExistsException(CategoryDoesNotExistsException e) {
        LOGGER.warn("Category does not exists: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.CATEGORY_DOES_NOT_EXISTS.getCode())
                                .businessErrorDescription(BusinessErrorCodes.CATEGORY_DOES_NOT_EXISTS.getDescription())
                                .error(e.getMessage())
                                .build()
                );
    }
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleProductNotFoundException(ProductNotFoundException e) {
        LOGGER.warn("Product does not exist: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.PRODUCT_NOT_FOUND.getCode())
                                .businessErrorDescription(BusinessErrorCodes.PRODUCT_NOT_FOUND.getDescription())
                                .error(e.getMessage())
                                .build()
                );
    }
    @ExceptionHandler(ParentCategoryCycleException.class)
    public ResponseEntity<ExceptionResponse> handleParentCategoryCycleException(ParentCategoryCycleException e) {
        LOGGER.warn("Parent category cycle exception: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.PARENT_CATEGORY_CYCLE_EXCEPTION.getCode())
                                .businessErrorDescription(BusinessErrorCodes.PARENT_CATEGORY_CYCLE_EXCEPTION.getDescription())
                                .error(e.getMessage())
                                .build()
                );
    }
    @ExceptionHandler(InvalidParentCategoryException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidParentCategoryException(InvalidParentCategoryException e){
        LOGGER.warn("Invalid parent category: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.INVALID_PARENT_CATEGORY_EXCEPTION.getCode())
                                .businessErrorDescription(BusinessErrorCodes.INVALID_PARENT_CATEGORY_EXCEPTION.getDescription())
                                .error(e.getMessage())
                                .build()
                );
    }
    @ExceptionHandler(InvalidDiscountException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidDiscountException(InvalidDiscountException e) {
        LOGGER.warn("Invalid discount: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.INVALID_DISCOUNT_EXCEPTION.getCode())
                                .businessErrorDescription(BusinessErrorCodes.INVALID_DISCOUNT_EXCEPTION.getDescription())
                                .error(e.getMessage())
                                .build());
    }
    @ExceptionHandler(DiscountNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleDiscountNotFoundException(DiscountNotFoundException e) {
        LOGGER.warn("Discount not found: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.DISCOUNT_NOT_FOUND.getCode())
                                .businessErrorDescription(BusinessErrorCodes.DISCOUNT_NOT_FOUND.getDescription())
                                .error(e.getMessage())
                                .build()
                );

    }
    @ExceptionHandler(UnauthorizedUserAccessException.class)
    public ResponseEntity<ExceptionResponse> handleUnauthroizedUserAccessException(UnauthorizedUserAccessException e){
        LOGGER.warn("Unauthorized user: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.UNAUTHORIZED_USER_ACCESS.getCode())
                                .businessErrorDescription(BusinessErrorCodes.UNAUTHORIZED_USER_ACCESS.getDescription())
                                .error(e.getMessage())
                                .build()
                );
    }






}
