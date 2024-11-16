package com.kodat.of.halleyecommerce.order;

import com.kodat.of.halleyecommerce.address.Address;
import com.kodat.of.halleyecommerce.address.AddressRepository;
import com.kodat.of.halleyecommerce.cart.Cart;
import com.kodat.of.halleyecommerce.cart.service.impl.CartServiceImpl;
import com.kodat.of.halleyecommerce.dto.address.AddressDto;
import com.kodat.of.halleyecommerce.dto.order.EmailConsumerDto;
import com.kodat.of.halleyecommerce.dto.order.NonMemberInfoDto;
import com.kodat.of.halleyecommerce.dto.order.OrderDto;
import com.kodat.of.halleyecommerce.dto.order.OrderResponseDto;
import com.kodat.of.halleyecommerce.exception.*;
import com.kodat.of.halleyecommerce.mapper.address.AddressMapper;
import com.kodat.of.halleyecommerce.mapper.order.OrderMapper;
import com.kodat.of.halleyecommerce.order.enums.Status;
import com.kodat.of.halleyecommerce.user.*;
import com.kodat.of.halleyecommerce.util.*;
import com.kodat.of.halleyecommerce.validator.CartValidator;
import com.kodat.of.halleyecommerce.validator.RoleValidator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.*;

@Service
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

    public OrderServiceImpl(OrderRepository orderRepository, RoleValidator roleValidator, CartValidator cartValidator, AddressRepository addressRepository, ShippingUtils shippingUtils, UnauthenticatedUtils unauthenticatedUtils, GuestUserRepository guestUserRepository, CartServiceImpl cartService, StockUtils stockUtils, OrderUtils orderUtils, OrderEmailProducer orderEmailProducer, OrderEmailUtils orderEmailUtils) {
        this.orderRepository = orderRepository;
        this.roleValidator = roleValidator;
        this.cartValidator = cartValidator;
        this.addressRepository = addressRepository;
        this.shippingUtils = shippingUtils;
        this.unauthenticatedUtils = unauthenticatedUtils;
        this.guestUserRepository = guestUserRepository;
        this.cartService = cartService;
        this.stockUtils = stockUtils;
        this.orderUtils = orderUtils;
        this.orderEmailProducer = orderEmailProducer;
        this.orderEmailUtils = orderEmailUtils;
    }


    @Transactional
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

    public OrderResponseDto createOrderForMember(OrderDto orderDto, Authentication connectedUser) {
        roleValidator.verifyUserRole(connectedUser);
        CustomUserDetails customUserDetails = (CustomUserDetails) connectedUser.getPrincipal();
        User user = customUserDetails.getUser();
        Cart cart = cartValidator.validateCartAndUser(connectedUser);
        //Validate stock before to check out
        orderUtils.validateCartStock(cart);
        Address address = addressRepository.findById(orderDto.getAddressId())
                .orElseThrow(() -> new AddressNotFoundException("Address not found"));
        BigDecimal totalPrice = orderUtils.calculateTotalPrice(cart.getItems());
        BigDecimal shippingCost = shippingUtils.calculateShippingCost(totalPrice);
        Order order = OrderMapper.createOrderForMemberUser(user, totalPrice, shippingCost, address.getId());
        List<OrderItem> orderItems = OrderMapper.toOrderItemList(cart.getItems(), order);
        order.setOrderItems(orderItems);
        orderRepository.save(order);
        orderItems.forEach(item ->
                stockUtils.updateStock(item.getProduct().getId(), item.getQuantity()));
        EmailConsumerDto emailConsumerDto = orderEmailUtils.setEmailConsumerDto(user , order);
        orderEmailProducer.sendOrderToQueueForMember(emailConsumerDto);
        return OrderMapper.toOrderResponseDto(order);
    }


    public OrderResponseDto createOrderForNonMember(OrderDto orderDto) {
        NonMemberInfoDto nonMemberInfoDto = orderDto.getNonMemberInfoDto();
        GuestUser guestUser = orderUtils.getOrCreateGuestUser(nonMemberInfoDto);
        orderUtils.validateRegisteredUserForGuestOrder(guestUser);
        Cart cart = unauthenticatedUtils.getOrCreateCart();
        orderUtils.validateCartStock(cart);
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
        orderRepository.save(order);
        orderItems.forEach(item ->
                stockUtils.updateStock(item.getProduct().getId(), item.getQuantity()));
        EmailConsumerDto emailConsumerDto = orderEmailUtils.setEmailConsumerDto(guestUser,order);
        orderEmailProducer.sendOrderToQueueNonMember(emailConsumerDto);
        return OrderMapper.toOrderResponseDto(order);
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
    public OrderResponseDto getOrderById(Long orderId, Authentication connectedUser) {
        roleValidator.verifyUserRole(connectedUser);
        cartValidator.validateCartAndUser(connectedUser);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        return OrderMapper.toOrderResponseDto(order);
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
    public OrderResponseDto updateOrderStatus(Long orderId, Status status, Authentication connectedUser) {
        roleValidator.verifyAdminRole(connectedUser);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        order.setStatus(status);
        orderRepository.save(order);
        return OrderMapper.toOrderResponseDto(order);
    }

    @Override
    public List<OrderResponseDto> getOrdersByDateRange(String startDate, String endDate, Authentication connectedUser) {
        roleValidator.verifyAdminRole(connectedUser);
        LocalDateTime startDateTime = LocalDateTime.parse(startDate);
        LocalDateTime endDateTime = LocalDateTime.parse(endDate);
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

    private boolean isAuthenticated(Authentication connectedUser) {
        return connectedUser != null && connectedUser.isAuthenticated();
    }

}
