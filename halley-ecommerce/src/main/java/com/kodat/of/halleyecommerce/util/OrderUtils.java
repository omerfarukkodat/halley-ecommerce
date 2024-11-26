package com.kodat.of.halleyecommerce.util;

import com.kodat.of.halleyecommerce.cart.Cart;
import com.kodat.of.halleyecommerce.cart.CartItem;
import com.kodat.of.halleyecommerce.dto.order.NonMemberInfoDto;
import com.kodat.of.halleyecommerce.exception.UserAlreadyExistsException;
import com.kodat.of.halleyecommerce.user.GuestUser;
import com.kodat.of.halleyecommerce.user.GuestUserRepository;
import com.kodat.of.halleyecommerce.user.UserRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class OrderUtils {
    private final GuestUserRepository guestUserRepository;
    private final StockUtils stockUtils;
    private final UserRepository userRepository;

    public OrderUtils(GuestUserRepository guestUserRepository, StockUtils stockUtils, UserRepository userRepository) {
        this.guestUserRepository = guestUserRepository;
        this.stockUtils = stockUtils;
        this.userRepository = userRepository;
    }


    public GuestUser getOrCreateGuestUser(NonMemberInfoDto nonMemberInfoDto) {
         return  guestUserRepository.findByEmail(nonMemberInfoDto.getEmail())
                 .orElseGet(() -> GuestUser.builder()
                .firstName(nonMemberInfoDto.getFirstName())
                .lastName(nonMemberInfoDto.getLastName())
                .email(nonMemberInfoDto.getEmail())
                .phoneNumber(nonMemberInfoDto.getPhoneNumber())
                .build());
    }

    public void validateCartItemStock(Cart cart) {
        for (CartItem item : cart.getItems()) {
            stockUtils.validateStockBeforeCheckout(item.getProduct().getId(), item.getQuantity());
        }
    }



    public void validateRegisteredUserForGuestOrder(GuestUser guestUser){
        if (userRepository.findByEmail(guestUser.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("You can not create order with this email.Because this email address already registered.");
        }
    }

    public BigDecimal calculateTotalPrice(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(item -> item.getProduct().getDiscountedPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }







}
