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



}
