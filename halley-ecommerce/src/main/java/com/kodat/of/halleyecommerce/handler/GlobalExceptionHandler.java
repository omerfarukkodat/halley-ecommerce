package com.kodat.of.halleyecommerce.handler;

import com.kodat.of.halleyecommerce.exception.*;
import com.kodat.of.halleyecommerce.exception.AddressAlreadyExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e) {
        LOGGER.error("Unexpected error occurred: {}", e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.INTERNAL_SERVER_ERROR.getCode())
                                .businessErrorDescription(BusinessErrorCodes.INTERNAL_SERVER_ERROR.getDescription())
                                .error(e.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException e) {
        LOGGER.warn("Validation error: {}", e.getMessage());
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException e) {
        LOGGER.warn("Constraint violation: {}", e.getMessage());
        Map<String, String> errors = new HashMap<>();
        e.getConstraintViolations().forEach(violation ->
                errors.put(violation.getPropertyPath().toString(), violation.getMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

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

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleAuthenticationException(AuthenticationException e) {
        LOGGER.warn("Authentication failed: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.AUTHENTICATION_FAILED.getCode())
                                .businessErrorDescription(BusinessErrorCodes.AUTHENTICATION_FAILED.getDescription())
                                .error(e.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleBadCredentialsException(BadCredentialsException e) {
        LOGGER.warn("Bad credentials: {}", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.BAD_CREDENTIALS_ACCESS.getCode())
                                .businessErrorDescription(BusinessErrorCodes.BAD_CREDENTIALS_ACCESS.getDescription())
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

    @ExceptionHandler(BrandNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleBrandNotFoundException(BrandNotFoundException e) {
        LOGGER.warn("Brand not found: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.BRAND_NOT_FOUND.getCode())
                                .businessErrorDescription(BusinessErrorCodes.BRAND_NOT_FOUND.getDescription())
                                .error(e.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(ProductAttributeNotFound.class)
    public ResponseEntity<ExceptionResponse> handleProductAttributeNotFountException(ProductAttributeNotFound e) {
        LOGGER.warn("Product attribute not found: {}", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.PRODUCT_ATTRIBUTE_NOT_FOUND.getCode())
                                .businessErrorDescription(BusinessErrorCodes.PRODUCT_ATTRIBUTE_NOT_FOUND.getDescription())
                                .error(e.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(ProductAttributeAlreadyExists.class)
    public ResponseEntity<ExceptionResponse> handleProductAttributeAlreadyExists(ProductAttributeAlreadyExists e) {
        LOGGER.warn("Product attribute already exists: {}", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.PRODUCT_ATTRIBUTE_ALREADY_EXISTS.getCode())
                                .businessErrorDescription(BusinessErrorCodes.PRODUCT_ATTRIBUTE_ALREADY_EXISTS.getDescription())
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

    @ExceptionHandler(BrandAlreadyExists.class)
    public ResponseEntity<ExceptionResponse> handleCategoryNotFoundException(BrandAlreadyExists message) {
        LOGGER.warn("Brand already exists: {}", message.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.BRAND_ALREADY_EXISTS.getCode())
                                .businessErrorDescription(BusinessErrorCodes.BRAND_ALREADY_EXISTS.getDescription())
                                .error(message.getMessage())
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

    @ExceptionHandler(BlogPostNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleBlogPostNotFoundException(BlogPostNotFoundException e) {
        LOGGER.warn("Blog post not found: {}", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.BLOGPOST_NOT_FOUND.getCode())
                                .businessErrorDescription(BusinessErrorCodes.BLOGPOST_NOT_FOUND.getDescription())
                                .error(e.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(ShowcaseNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleShowcaseNotFoundException(ShowcaseNotFoundException e) {
        LOGGER.warn("Showcase not found: {}", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.SHOWCASE_NOT_FOUND.getCode())
                                .businessErrorDescription(BusinessErrorCodes.SHOWCASE_NOT_FOUND.getDescription())
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
    public ResponseEntity<ExceptionResponse> handleInvalidParentCategoryException(InvalidParentCategoryException e) {
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
    public ResponseEntity<ExceptionResponse> handleUnauthroizedUserAccessException(UnauthorizedUserAccessException e) {
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

    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleAddressNotFoundException(AddressNotFoundException e) {
        LOGGER.warn("Address does not exist: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.ADDRESS_NOT_FOUND.getCode())
                                .businessErrorDescription(BusinessErrorCodes.ADDRESS_NOT_FOUND.getDescription())
                                .error(e.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(AddressAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleAddressAlreadyExistsException(AddressAlreadyExistsException e) {
        LOGGER.warn("Address already exists: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.ADDRESS_ALREADY_EXISTS_EXCEPTION.getCode())
                                .businessErrorDescription(BusinessErrorCodes.ADDRESS_ALREADY_EXISTS_EXCEPTION.getDescription())
                                .error(e.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(EmptyCartException.class)
    public ResponseEntity<ExceptionResponse> handleEmptyCartException(EmptyCartException e) {
        LOGGER.warn("Empty cart exception: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.EMPTY_CART.getCode())
                                .businessErrorDescription(BusinessErrorCodes.EMPTY_CART.getDescription())
                                .error(e.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleCartNotFoundException(CartNotFoundException e) {
        LOGGER.warn("Cart not found: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.CART_NOT_FOUND.getCode())
                                .businessErrorDescription(BusinessErrorCodes.CART_NOT_FOUND.getDescription())
                                .error(e.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleCartItemNotFoundException(CartItemNotFoundException e) {
        LOGGER.warn("Cart item not found: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.CART_ITEM_NOT_FOUND.getCode())
                                .businessErrorDescription(BusinessErrorCodes.CART_ITEM_NOT_FOUND.getDescription())
                                .error(e.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ExceptionResponse> handleInsufficientStockException(InsufficientStockException e) {
        LOGGER.warn("Insufficient stock: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.INSUFFICIENT_STOCK.getCode())
                                .businessErrorDescription(BusinessErrorCodes.INSUFFICIENT_STOCK.getDescription())
                                .error(e.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleOrderNotFoundException(OrderNotFoundException e) {
        LOGGER.warn("Order not found: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.ORDER_NOT_FOUND.getCode())
                                .businessErrorDescription(BusinessErrorCodes.ORDER_NOT_FOUND.getDescription())
                                .error(e.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidTokenException(InvalidTokenException e) {
        LOGGER.warn("Invalid token: {}", e.getMessage());
        return ResponseEntity
                .status((HttpStatus.CONFLICT))
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.INVALID_TOKEN.getCode())
                                .businessErrorDescription(BusinessErrorCodes.INVALID_TOKEN.getDescription())
                                .error(e.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(DuplicatePasswordException.class)
    public ResponseEntity<ExceptionResponse> handleDuplicatePasswordException(DuplicatePasswordException e) {
        LOGGER.warn("Duplicate password: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.DUPLICATE_PASSWORD.getCode())
                                .businessErrorDescription(BusinessErrorCodes.DUPLICATE_PASSWORD.getDescription())
                                .error(e.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(RateLimiterAttemptException.class)
    public ResponseEntity<ExceptionResponse> handleRateLimiterAttemptException(RateLimiterAttemptException e) {
        LOGGER.warn("Rate limiter attempt exception: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.RATE_LIMITER_ATTEMPT.getCode())
                                .businessErrorDescription(BusinessErrorCodes.RATE_LIMITER_ATTEMPT.getDescription())
                                .error(e.getMessage())
                                .build()
                );
    }


}
