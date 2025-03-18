package com.kodat.of.halleyecommerce.order;

import com.kodat.of.halleyecommerce.address.Address;
import com.kodat.of.halleyecommerce.address.AddressRepository;
import com.kodat.of.halleyecommerce.cart.Cart;
import com.kodat.of.halleyecommerce.cart.service.impl.CartServiceImpl;
import com.kodat.of.halleyecommerce.dto.address.AddressDto;
import com.kodat.of.halleyecommerce.dto.order.*;
import com.kodat.of.halleyecommerce.email.OrderEmailUtils;
import com.kodat.of.halleyecommerce.exception.*;
import com.kodat.of.halleyecommerce.mapper.address.AddressMapper;
import com.kodat.of.halleyecommerce.mapper.order.OrderMapper;
import com.kodat.of.halleyecommerce.order.enums.Status;
import com.kodat.of.halleyecommerce.user.*;
import com.kodat.of.halleyecommerce.util.*;
import com.kodat.of.halleyecommerce.validator.CartValidator;
import com.kodat.of.halleyecommerce.validator.RoleValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final RoleValidator roleValidator;
    private final CartValidator cartValidator;
    private final AddressRepository addressRepository;
    private final ShippingUtils shippingUtils;
    private final UnauthenticatedUtils unauthenticatedUtils;
    private final GuestUserRepository guestUserRepository;
    private final CartServiceImpl cartService;
    private final StockUtils stockUtils;
    private final OrderUtils orderUtils;
    private final OrderEmailProducer orderEmailProducer;
    private final OrderEmailUtils orderEmailUtils;

    @Override
    public OrderResponseDto createOrderFromCart(OrderDto orderDto, Authentication connectedUser) {
        if (cartService.isEmptyCart(connectedUser)) {
            throw new EmptyCartException("You can't create an order without a cart.");
        }
        OrderResponseDto createdOrder = isAuthenticated(connectedUser)
                ? createOrderForMember(orderDto, connectedUser)
                : createOrderForNonMember(orderDto);
        cartService.removeAllCartItemFromCart(connectedUser);
        return createdOrder;
    }

    private void handlePaymentMethod(OrderResponseDto orderResponse, PaymentMethod paymentMethod,Address address) {
        if (paymentMethod == PaymentMethod.IBAN_TRANSFER){
            orderResponse.setPaymentDetails("IBAN: TR00 1234 5678 9012 3456 78");
        } else if (paymentMethod == PaymentMethod.WHATSAPP) {
            String whatappMessage = generateWhatsappMessage(orderResponse,address);
            orderResponse.setPaymentDetails(whatappMessage);
            
        }
    }

    private String generateWhatsappMessage(OrderResponseDto orderResponse,Address address) {

        StringBuilder message = new StringBuilder();
        message.append("Merhaba, aşağıdaki siparişi vermek istiyorum:\n\n");
        for (OrderItemDto item : orderResponse.getOrderItems()) {
            message.append(item.getProduct().getName())
                    .append(" - ")
                    .append(item.getProduct().getProductCode())
                    .append(" - ")
                    .append(item.getProduct().getDiscountedPrice())
                    .append(" - ")
                    .append(item.getQuantity())
                    .append(" adet\n");
        }
        message.append("\nAdres:\n ").append(address.toString());
        message.append("\n\nLütfen siparişimi onaylar mısınız?");

        String encodedMessage = URLEncoder.encode(message.toString(), StandardCharsets.UTF_8);
        return "https://api.whatsapp.com/send?phone=905468732334&text=" + encodedMessage;
    }

    public OrderResponseDto createOrderForMember(OrderDto orderDto, Authentication connectedUser) {
        roleValidator.verifyUserRole(connectedUser);

        CustomUserDetails customUserDetails = (CustomUserDetails) connectedUser.getPrincipal();
        User user = customUserDetails.getUser();
        Cart cart = cartValidator.validateCartAndUser(connectedUser);
        //Validate stock before to check out
        orderUtils.validateCartItemStock(cart);

        Address address = addressRepository.findById(orderDto.getAddressId())
                .orElseThrow(() -> new AddressNotFoundException("Address not found"));

        BigDecimal totalPrice = orderUtils.calculateTotalPrice(cart.getItems());
        BigDecimal shippingCost = shippingUtils.calculateShippingCost(totalPrice);

        Order order = OrderMapper.createOrderForMemberUser(user, totalPrice, shippingCost, address.getId());
        List<OrderItem> orderItems = OrderMapper.toOrderItemList(cart.getItems(), order);
        order.setOrderItems(orderItems);

        OrderResponseDto orderResponseDto = OrderMapper.toOrderResponseDto(order);
        handlePaymentMethod(orderResponseDto,orderDto.getPaymentMethod(),address);
        orderRepository.save(order);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM EEEE, HH:mm", Locale.forLanguageTag("tr"));
        orderResponseDto.setCreatedAt(order.getCreatedAt().format(formatter));

        orderItems.forEach(item ->
                stockUtils.updateStock(item.getProduct().getId(), item.getQuantity()));

        EmailConsumerDto emailConsumerDto = orderEmailUtils.setEmailConsumerDto(user, order, true);
        orderEmailProducer.sendOrderToQueueForMember(emailConsumerDto);
        return orderResponseDto;
    }


    public OrderResponseDto createOrderForNonMember(OrderDto orderDto) {

        NonMemberInfoDto nonMemberInfoDto = orderDto.getNonMemberInfoDto();
        GuestUser guestUser = orderUtils.getOrCreateGuestUser(nonMemberInfoDto);
        orderUtils.validateRegisteredUserForGuestOrder(guestUser);

        Cart cart = unauthenticatedUtils.getOrCreateCart();
        orderUtils.validateCartItemStock(cart);

        guestUserRepository.save(guestUser);

        AddressDto addressDto = nonMemberInfoDto.getAddress();
        Address address = AddressMapper.toAddress(addressDto);
        address.setGuestUser(guestUser);
        addressRepository.save(address);
        orderDto.setAddressId(address.getId());

        BigDecimal totalPrice = orderUtils.calculateTotalPrice(cart.getItems());
        BigDecimal shippingCost = shippingUtils.calculateShippingCost(totalPrice);

        Order order = OrderMapper.createOrderEntityForNonMember(guestUser, totalPrice, shippingCost, address.getId());
        List<OrderItem> orderItems = OrderMapper.toOrderItemList(cart.getItems(), order);
        order.setOrderItems(orderItems);
        OrderResponseDto orderResponseDto = OrderMapper.toOrderResponseDto(order);
        handlePaymentMethod(orderResponseDto,orderDto.getPaymentMethod(),address);

        orderRepository.save(order);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM EEEE, HH:mm", Locale.forLanguageTag("tr"));
        orderResponseDto.setCreatedAt(order.getCreatedAt().format(formatter));
        orderResponseDto.setId(order.getId());


        orderItems.forEach(item ->
                stockUtils.updateStock(item.getProduct().getId(), item.getQuantity()));

        EmailConsumerDto emailConsumerDto = orderEmailUtils.setEmailConsumerDto(guestUser, order, true);
        orderEmailProducer.sendOrderToQueueNonMember(emailConsumerDto);
        return orderResponseDto;
    }

    @Override
    public List<OrderResponseDto> getAllOrders(Authentication connectedUser) {
        roleValidator.verifyUserRole(connectedUser);
        CustomUserDetails customUserDetails = (CustomUserDetails) connectedUser.getPrincipal();
        User user = customUserDetails.getUser();
        cartValidator.validateCartAndUser(connectedUser);

        List<Order> order = orderRepository.findAllByUser(user)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        return order.stream()
                .map(OrderMapper::toOrderResponseDto)
                .toList();
    }

    @Override
    public UserOrderSummaryDto getOrderById(Long orderId, Authentication connectedUser) {
        roleValidator.verifyUserRole(connectedUser);
        cartValidator.validateCartAndUser(connectedUser);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        Address address = addressRepository.findById(order.getAddressId())
                .orElseThrow(() -> new AddressNotFoundException("Address not found with id: " + order.getAddressId()));

        UserOrderSummaryDto userOrderSummaryDto = OrderMapper.toUserOrderSummaryDto(order);
        userOrderSummaryDto.setAddress(AddressMapper.toAddressDto(address));
        return userOrderSummaryDto;
    }



    @Override
    public List<OrderResponseDto> getAllOrdersForAdmin(Authentication connectedUser) {
        roleValidator.verifyAdminRole(connectedUser);
        List<Order> allOrders = orderRepository.findAll();
        return allOrders.stream()
                .map(OrderMapper::toOrderResponseDto)
                .toList();
    }

    @Override
    public void updateOrderStatus(Long orderId, Status status, Authentication connectedUser) {
        roleValidator.verifyAdminRole(connectedUser);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        order.setStatus(status);
        orderRepository.save(order);
        EmailConsumerDto emailConsumerDto = orderEmailUtils.setEmailConsumerDto(order.getUser() != null ? order.getUser() : order.getGuestUser(), order, false);
        orderEmailProducer.sendOrderShippedQueue(emailConsumerDto);
    }

    @Override
    public List<OrderResponseDto> getOrdersByDateRange(String startDate, String endDate, Authentication connectedUser) {
        roleValidator.verifyAdminRole(connectedUser);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime startDateTime = LocalDate.parse(startDate, formatter).atStartOfDay();
        LocalDateTime endDateTime = LocalDate.parse(endDate, formatter).atTime(23, 59, 59);
        List<Order> orderList = orderRepository.findByCreatedAtBetween(startDateTime, endDateTime)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        return orderList.stream()
                .map(OrderMapper::toOrderResponseDto)
                .toList();
    }

    @Override
    public BigDecimal getTotalSalesForAdmin(String startDate, String endDate, Authentication connectedUser) {
        roleValidator.verifyAdminRole(connectedUser);
        LocalDateTime startDateTime = LocalDateTime.parse(startDate);
        LocalDateTime endDateTime = LocalDateTime.parse(endDate);
        List<Order> orderList = orderRepository.findByCreatedAtBetween(startDateTime, endDateTime)
                .orElseThrow(() -> new OrderNotFoundException("Order not found these between days"));
        return orderList.stream()
                .map(Order::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public OrderSummaryDto getOrderSummary(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        return OrderMapper.toOrderSummaryDto(order);
    }

    private boolean isAuthenticated(Authentication connectedUser) {
        return connectedUser != null && connectedUser.isAuthenticated();
    }

}
