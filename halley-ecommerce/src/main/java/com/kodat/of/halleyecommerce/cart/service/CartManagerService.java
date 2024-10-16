package com.kodat.of.halleyecommerce.cart.service;

import com.kodat.of.halleyecommerce.cart.Cart;
import com.kodat.of.halleyecommerce.cart.CartItem;
import com.kodat.of.halleyecommerce.cart.CartItemRepository;
import com.kodat.of.halleyecommerce.cart.CartRepository;
import com.kodat.of.halleyecommerce.dto.cart.AddToCartRequest;
import com.kodat.of.halleyecommerce.dto.cart.CartDto;
import com.kodat.of.halleyecommerce.dto.cart.CartItemDto;
import com.kodat.of.halleyecommerce.dto.cart.CartSummaryDto;
import com.kodat.of.halleyecommerce.exception.ProductNotFoundException;
import com.kodat.of.halleyecommerce.exception.UserNotFoundException;
import com.kodat.of.halleyecommerce.mapper.cart.CartMapper;
import com.kodat.of.halleyecommerce.product.Product;
import com.kodat.of.halleyecommerce.product.ProductRepository;
import com.kodat.of.halleyecommerce.user.CustomUserDetails;
import com.kodat.of.halleyecommerce.user.User;
import com.kodat.of.halleyecommerce.validator.CartValidator;
import com.kodat.of.halleyecommerce.validator.RoleValidator;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class CartManagerService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    private final CartValidator cartValidator;
    private final RoleValidator roleValidator;

    public CartManagerService(CartRepository cartRepository, CartItemRepository cartItemRepository, ProductRepository productRepository, CartValidator cartValidator, RoleValidator roleValidator) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.cartValidator = cartValidator;
        this.roleValidator = roleValidator;
    }


    public CartDto addToCartForAuthenticatedUser(AddToCartRequest request, Authentication connectedUser) {
        CustomUserDetails customUserDetails = (CustomUserDetails) connectedUser.getPrincipal();
        User user = customUserDetails.getUser();
        if (user == null) {
            throw new UserNotFoundException("User must not be null");
        }

        roleValidator.verifyUserRole(connectedUser);
        Cart cart = cartValidator.validateCartAndUser(connectedUser);
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user); // Set the user
            cart = cartRepository.save(cart); // Save the new cart
        }
        return addProduct(CartMapper.INSTANCE.toCartDto(cart), request.getProductId(), request.getQuantity());
    }

    public CartDto addToCartForUnauthenticatedUser(AddToCartRequest request, HttpSession session) {
        CartDto sessionCart = (CartDto) session.getAttribute("cart");
        if (sessionCart == null) {
            sessionCart = new CartDto();
        }
        CartDto updatedCartDto = addProduct(sessionCart, request.getProductId(), request.getQuantity());
        session.setAttribute("cart", updatedCartDto);
        return updatedCartDto;
    }

    public CartDto mergeCarts(CartDto cartDto, List<CartItemDto> sessionCartItems) {
        for (CartItemDto sessionCartItem : sessionCartItems) {
            Product product = productRepository.findById(sessionCartItem.getProduct().getId())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found"));

            CartItem existingItem = findCartItemInDto(cartDto, product);
            if (existingItem != null) {
                existingItem.setQuantity(sessionCartItem.getQuantity() + existingItem.getQuantity());
            } else {
                cartDto.getItems().add(sessionCartItem);
            }
        }
        Cart mergedCart = cartRepository.save(CartMapper.INSTANCE.toCart(cartDto));
        return CartMapper.INSTANCE.toCartDto(mergedCart);
    }


    public CartDto getCart(Authentication connectedUser) {
        roleValidator.verifyUserRole(connectedUser);
        Cart cart = cartValidator.validateCartAndUser(connectedUser);
        return CartMapper.INSTANCE.toCartDto(cart);
    }

    public CartDto addProduct(CartDto cartDto, Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        // Quantity control
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity cannot be less than or equal to 0");
        }
        if (quantity > product.getStock()) {
            throw new IllegalArgumentException("Insufficient stock available. Only " + product.getStock() + " left.");
        }

        // Check if the cart has the same product
        CartItemDto existingItem = findItemByProductId(cartDto, productId);

        if (existingItem != null) {
            // If the exists , update to quantity
            int newQuantity = existingItem.getQuantity() + quantity;
            if (newQuantity > product.getStock()) {
                throw new IllegalArgumentException("Insufficient stock available. Only " + product.getStock() + " left.");
            }
            existingItem.setQuantity(newQuantity);

            // Update the cart items , and save the repo
            CartItem cartItem = cartItemRepository.findByProductIdAndCartId(productId, CartMapper.INSTANCE.toCart(cartDto).getId());
            if (cartItem != null) {
                cartItem.setQuantity(newQuantity);
                cartItemRepository.save(cartItem);  // update existing cart
            }
        } else {
            // If not exist create new one
            CartItemDto newItem = new CartItemDto();
            newItem.setProduct(CartMapper.INSTANCE.toProductResponseDto(product));
            newItem.setQuantity(quantity);
            cartDto.getItems().add(newItem);

            // save the new cart item to the repo
            CartItem cartItem = new CartItem();
            cartItem.setCart(CartMapper.INSTANCE.toCart(cartDto));
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }

        return cartDto;
    }


    public CartDto removeFromAuthenticatedUser(Long productId, Authentication connectedUser) {
        CustomUserDetails userDetails = (CustomUserDetails) connectedUser.getPrincipal();
        User user = userDetails.getUser();

        //Get the user's cart
        Cart cart = cartRepository.findByUser(user);
        if (cart != null) {
            CartItem cartItem = cartItemRepository.findByProductIdAndCartId(productId, cart.getId());
            if (cartItem != null) {
                cartItemRepository.delete(cartItem);
            } else {
                throw new ProductNotFoundException("Product not found in Cart");
            }
        }
        return CartMapper.INSTANCE.toCartDto(cart);
    }

    public CartDto removeFromUnAuthenticatedUser(Long productId, HttpSession session) {
        CartDto sessionCart = (CartDto) session.getAttribute("cart");
        if (sessionCart == null) {
            throw new ProductNotFoundException("Cart does not exist in session");
        }
        CartItemDto existingItem = findItemByProductId(sessionCart, productId);
        if (existingItem != null) {
            sessionCart.getItems().remove(existingItem);
            session.setAttribute("cart", sessionCart);
        } else {
            throw new ProductNotFoundException("Product does not exist in session");
        }
        return sessionCart;
    }

    private CartItem findCartItemInDto(CartDto cartDto, Product product) {
        return cartDto.getItems().stream()
                .map(CartMapper.INSTANCE::toCartItem)
                .filter(cartItem -> cartItem.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);
    }

    private CartItemDto findItemByProductId(CartDto cartDto, Long productId) {
        return cartDto.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    public CartDto decreaseProductQuantity(Authentication connectedUser, HttpSession session, Long productId, Integer quantity) {
        if (connectedUser != null && connectedUser.isAuthenticated()) {
            return decreaseProductQuantityForAuthenticatedUser(productId, quantity, connectedUser);
        } else {
            return decreaseProductQuantityForUnauthenticatedUser(productId, quantity, session);
        }
    }

    private CartDto decreaseProductQuantityForUnauthenticatedUser(Long productId, Integer quantity, HttpSession session) {
        CartDto sessionCart = (CartDto) session.getAttribute("cart");
        if (sessionCart == null) {
            throw new ProductNotFoundException("Cart does not exist in session");
        }
        CartItemDto cartItemDto = findItemByProductId(sessionCart, productId);
        if (cartItemDto == null) {
            throw new ProductNotFoundException("Product does not exist in session");
        }
        int newQuantity = cartItemDto.getQuantity() - quantity;
        if (newQuantity <= 0) {
            sessionCart.getItems().remove(cartItemDto);
            session.setAttribute("cart", sessionCart);
            return sessionCart;
        }
        cartItemDto.setQuantity(newQuantity);
        session.setAttribute("cart", sessionCart);
        return sessionCart;
    }

    private CartDto decreaseProductQuantityForAuthenticatedUser(Long productId, Integer quantity, Authentication connectedUser) {
        Cart cart = cartValidator.validateCartAndUser(connectedUser);
        CartItem cartItem = cartItemRepository.findByProductIdAndCartId(productId, cart.getId());
        if (cartItem == null) {
            throw new ProductNotFoundException("Product not found in Cart");
        }
        int newQuantity = cartItem.getQuantity() - quantity;
        if (newQuantity <= 0) {
            // If the new quantity equal or less than 0 , remove the product from cart
            cartItemRepository.delete(cartItem);
            return CartMapper.INSTANCE.toCartDto(cart);
        }
        cartItem.setQuantity(newQuantity);
        cartItemRepository.save(cartItem);
        return CartMapper.INSTANCE.toCartDto(cart);
    }

    public void clearAuthenticatedCart(Authentication connectedUser) {
        Cart cart = cartValidator.validateCartAndUser(connectedUser);
        if (cart != null) {
            cartItemRepository.deleteAllByCartId(cart.getId());
        }

    }

    public void clearUnauthenticatedCart(HttpSession session) {
        CartDto sessionCart = (CartDto) session.getAttribute("cart");
        if (sessionCart == null) {
            throw new ProductNotFoundException("Cart does not exist in session");
        }
        sessionCart.getItems().clear();
        session.setAttribute("cart", sessionCart);
    }

    public CartSummaryDto getCartSummaryAuthenticated(Authentication connectedUser) {
        Cart cart = cartValidator.validateCartAndUser(connectedUser);
        if (cart == null) {
            throw new ProductNotFoundException("Cart does not exist in session");
        }
        BigDecimal totalPrice = cart.getItems().stream()
                .map(item -> item.getProduct().getDiscountedPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        int totalQuantity = cart.getItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
        return CartSummaryDto.builder()
                .cartItems(CartMapper.INSTANCE.toCartItemDtoList(cart.getItems()))
                .totalPrice(totalPrice)
                .totalItems(totalQuantity)
                .build();
    }

    public CartSummaryDto getCartSummaryUnauthenticated(HttpSession session) {
        Object cartItemsObject = session.getAttribute("cartItems");
        if (cartItemsObject instanceof List<?> cartItemsList) {
            if (!cartItemsList.isEmpty() && cartItemsList.getFirst() instanceof CartItem) {
                @SuppressWarnings("unchecked")
                List<CartItem> cartItems = (List<CartItem>) cartItemsList;

                if (cartItems.isEmpty()) {
                    throw new ProductNotFoundException("Cart does not exist in session");
                }
                BigDecimal totalPrice = cartItems.stream()
                        .map(item -> item.getProduct().getDiscountedPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                int totalQuantity = cartItems.stream()
                        .mapToInt(CartItem::getQuantity)
                        .sum();

                return CartSummaryDto.builder()
                        .cartItems(CartMapper.INSTANCE.toCartItemDtoList(cartItems))
                        .totalPrice(totalPrice)
                        .totalItems(totalQuantity)
                        .build();
            }
        }
        throw new ProductNotFoundException("Cart does not exist in session");
    }

    public Boolean isEmptyForAuthenticated(Authentication connectedUser) {
        Cart cart = cartValidator.validateCartAndUser(connectedUser);
        return cart == null || cart.getItems().isEmpty();
    }

    public Boolean isEmptyForUnauthenticated(HttpSession session) {
        CartDto sessionCart = (CartDto) session.getAttribute("cart");
        return sessionCart == null || sessionCart.getItems().isEmpty();
    }

    public CartDto updateQuantitiesForAuthenticated(Authentication connectedUser, Map<Long,Integer> productQuantities) {
        Cart cart = cartValidator.validateCartAndUser(connectedUser);
        if (cart == null) {
            throw new ProductNotFoundException("Cart does not exist in session");
        }
        AtomicBoolean isUpdated = new AtomicBoolean(false);
        cart.getItems().forEach(item -> {
            Long productId = item.getProduct().getId();
            if (productQuantities.containsKey(productId)) {
                Integer newQuantity = productQuantities.get(productId);
                if (isQuantityUpdated(newQuantity, item.getQuantity())) {
                    item.setQuantity(newQuantity);
                    isUpdated.set(true);
                }
            }
        });
        if (isUpdated.get()){
            cartRepository.save(cart);
        }
        return CartMapper.INSTANCE.toCartDto(cart);
    }

    public CartDto updateQuantitiesForUnauthenticated(HttpSession session, Map<Long, Integer> productQuantities) {
        Object sessionCartObject = session.getAttribute("cart");
        if (!(sessionCartObject instanceof CartDto sessionCart) || sessionCart.getItems() == null) {
            throw new ProductNotFoundException("Cart does not exist in session");
        }
        AtomicBoolean isUpdated = new AtomicBoolean(false);
        sessionCart.getItems().forEach(item -> {
            Long productId = item.getProduct().getId();
            if (productQuantities.containsKey(productId)) {
                Integer newQuantity = productQuantities.get(productId);
                if (isQuantityUpdated(newQuantity, item.getQuantity())) {
                    isUpdated.set(true);
                    item.setQuantity(newQuantity);
                }
            }
        });
        if (isUpdated.get()){
            session.setAttribute("cart", sessionCart);
        }
        return sessionCart;
    }

    private boolean isQuantityUpdated(Integer newQuantity, Integer currentQuantity) {
        return newQuantity != null && newQuantity > 0 && !newQuantity.equals(currentQuantity);
    }
}
