package com.kodat.of.halleyecommerce.util;

import com.kodat.of.halleyecommerce.order.Order;
import com.kodat.of.halleyecommerce.user.EmailService;
import com.kodat.of.halleyecommerce.user.GuestUser;
import com.kodat.of.halleyecommerce.user.User;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
public class OrderEmailUtils {
    private final EmailService emailService;

    public OrderEmailUtils(EmailService emailService) {
        this.emailService = emailService;
    }


    public void sendEmailForOrderSummary(Order order, User user) {
        Instant instant = Instant.now();
        ZoneId zoneId = ZoneId.of("Asia/Istanbul");
        ZonedDateTime zdt = instant.atZone(zoneId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM EEEE, HH:mm", Locale.forLanguageTag("tr"));
        String formattedOrderDate = zdt.format(formatter);
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("customerName", user.getFirstName());
        orderData.put("customerLastName", user.getLastName());
        orderData.put("orderDate", formattedOrderDate);
        orderData.put("orderItems", order.getOrderItems());
        orderData.put("shippingCost", order.getShippingCost());
        orderData.put("finalPrice", order.getFinalPrice());
        emailService.sendOrderSummaryEmail(user.getEmail(), orderData);
    }

    public void sendEmailForOrderSummaryNonMember(Order order, GuestUser guestUser) {
        Instant instant = Instant.now();
        ZoneId zoneId = ZoneId.of("Asia/Istanbul");
        ZonedDateTime zdt = instant.atZone(zoneId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM EEEE, HH:mm", Locale.forLanguageTag("tr"));
        String formattedOrderDate = zdt.format(formatter);
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("customerName", guestUser.getFirstName());
        orderData.put("customerLastName", guestUser.getLastName());
        orderData.put("orderDate", formattedOrderDate);
        orderData.put("orderItems", order.getOrderItems());
        orderData.put("shippingCost", order.getShippingCost());
        orderData.put("finalPrice", order.getFinalPrice());
        emailService.sendOrderSummaryEmail(guestUser.getEmail(), orderData);
    }
}
