package com.kodat.of.halleyecommerce.util;

import com.kodat.of.halleyecommerce.dto.order.EmailConsumerDto;
import com.kodat.of.halleyecommerce.mapper.order.OrderItemMapper;
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

    public void sendEmailForOrderSummary(EmailConsumerDto emailConsumerDto) {
        String formattedOrderDate = createDate();
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("customerName", emailConsumerDto.getFirstName());
        orderData.put("customerLastName", emailConsumerDto.getLastName());
        orderData.put("orderDate", formattedOrderDate);
        orderData.put("orderItems", emailConsumerDto.getOrderItems());
        orderData.put("shippingCost", emailConsumerDto.getShippingCost());
        orderData.put("finalPrice", emailConsumerDto.getFinalPrice());
        emailService.sendOrderSummaryEmail(emailConsumerDto.getEmail(), orderData);
    }

    public void sendEmailForOrderShipped(EmailConsumerDto emailConsumerDto) {
        String formattedOrderDate = createDate();
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("customerName", emailConsumerDto.getFirstName());
        orderData.put("customerLastName", emailConsumerDto.getLastName());
        orderData.put("shippedDate", formattedOrderDate);
        orderData.put("shippingCost", emailConsumerDto.getShippingCost());
        orderData.put("finalPrice", emailConsumerDto.getFinalPrice());
        emailService.sendOrderShippedEmail(emailConsumerDto.getEmail(), orderData);
    }

    public EmailConsumerDto setEmailConsumerDto(Object object, Order order, boolean includeOrderItems) {
        User user;
        GuestUser guestUser;
        EmailConsumerDto emailConsumerDto;
        if (object instanceof User) {
            user = (User) object;
            emailConsumerDto = EmailConsumerDto.builder()
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .build();
        } else {
            guestUser = (GuestUser) object;
            emailConsumerDto = EmailConsumerDto.builder()
                    .firstName(guestUser.getFirstName())
                    .lastName(guestUser.getLastName())
                    .email(guestUser.getEmail())
                    .build();
        }
        if (includeOrderItems) {
            emailConsumerDto.setOrderItems(OrderItemMapper.toEmailOrderItemDtoList(order.getOrderItems()));
        }
        emailConsumerDto.setShippingCost(order.getShippingCost());
        emailConsumerDto.setFinalPrice(order.getFinalPrice());
        return emailConsumerDto;
    }

    private static String createDate() {
        Instant instant = Instant.now();
        ZoneId zoneId = ZoneId.of("Asia/Istanbul");
        ZonedDateTime zdt = instant.atZone(zoneId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM EEEE, HH:mm", Locale.forLanguageTag("tr"));
        return zdt.format(formatter);
    }


}
