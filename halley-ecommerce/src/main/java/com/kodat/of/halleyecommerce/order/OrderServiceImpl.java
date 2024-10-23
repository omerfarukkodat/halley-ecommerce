package com.kodat.of.halleyecommerce.order;

import com.kodat.of.halleyecommerce.address.Address;
import com.kodat.of.halleyecommerce.address.AddressRepository;
import com.kodat.of.halleyecommerce.cart.Cart;
import com.kodat.of.halleyecommerce.cart.CartItem;
import com.kodat.of.halleyecommerce.dto.order.OrderDto;
import com.kodat.of.halleyecommerce.exception.AddressNotFoundException;
import com.kodat.of.halleyecommerce.exception.OrderNotFoundException;
import com.kodat.of.halleyecommerce.mapper.order.OrderMapper;
import com.kodat.of.halleyecommerce.order.enums.Status;
import com.kodat.of.halleyecommerce.user.CustomUserDetails;
import com.kodat.of.halleyecommerce.user.User;
import com.kodat.of.halleyecommerce.util.ShippingUtils;
import com.kodat.of.halleyecommerce.validator.CartValidator;
import com.kodat.of.halleyecommerce.validator.RoleValidator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final RoleValidator roleValidator;
    private final CartValidator cartValidator;
    private final AddressRepository addressRepository;
    private final ShippingUtils shippingUtils;

    public OrderServiceImpl(OrderRepository orderRepository, RoleValidator roleValidator, CartValidator cartValidator, AddressRepository addressRepository, ShippingUtils shippingUtils) {
        this.orderRepository = orderRepository;
        this.roleValidator = roleValidator;
        this.cartValidator = cartValidator;
        this.addressRepository = addressRepository;
        this.shippingUtils = shippingUtils;
    }


    @Override
    public OrderDto createOrderFromCart(OrderDto orderDto, Authentication connectedUser) {
       roleValidator.verifyUserRole(connectedUser);
        CustomUserDetails customUserDetails = (CustomUserDetails) connectedUser.getPrincipal();
        User user = customUserDetails.getUser();
        Cart cart = cartValidator.validateCartAndUser(connectedUser);
        Address address = addressRepository.findById(orderDto.getAddressId())
                .orElseThrow(() -> new AddressNotFoundException("Address not found"));
        BigDecimal totalPrice = calculateTotalPrice(cart.getItems());
        BigDecimal shippingCost = shippingUtils.calculateShippingCost(totalPrice);
        Order order = Order.builder()
                .user(user)
                .totalPrice(calculateTotalPrice(cart.getItems()))
                .shippingCost(shippingCost)
                .finalPrice(totalPrice.add(shippingCost))
                .addressId(address.getId())
                .status(Status.HAZIRLANIYOR)
                .build();
        List<OrderItem> orderItems = OrderMapper.toOrderItemList(cart.getItems(),order);
        order.setOrderItems(orderItems);
        orderRepository.save(order);
        return OrderMapper.toOrderDto(order);

    }

    @Override
    public List<OrderDto> getAllOrders(Authentication connectedUser) {
        roleValidator.verifyUserRole(connectedUser);
        CustomUserDetails customUserDetails = (CustomUserDetails) connectedUser.getPrincipal();
        User user = customUserDetails.getUser();
        cartValidator.validateCartAndUser(connectedUser);
        List<Order> order = orderRepository.findAllByUser(user)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        return order.stream()
                .map(OrderMapper::toOrderDto)
                .toList();
    }

    @Override
    public OrderDto getOrderById(Long orderId, Authentication connectedUser) {
        roleValidator.verifyUserRole(connectedUser);
        cartValidator.validateCartAndUser(connectedUser);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        return OrderMapper.toOrderDto(order);
    }

    @Override
    public List<OrderDto> getAllOrdersForAdmin(Authentication connectedUser) {
        roleValidator.verifyAdminRole(connectedUser);
        List<Order> allOrders = orderRepository.findAll();
        return allOrders.stream()
                .map(OrderMapper::toOrderDto)
                .toList();
    }

    @Override
    public OrderDto updateOrderStatus(Long orderId, Status status, Authentication connectedUser) {
        roleValidator.verifyAdminRole(connectedUser);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        order.setStatus(status);
        orderRepository.save(order);
        return OrderMapper.toOrderDto(order);
    }

    @Override
    public List<OrderDto> getOrdersByDateRange(String startDate, String endDate, Authentication connectedUser) {
        roleValidator.verifyAdminRole(connectedUser);
        LocalDateTime startDateTime = LocalDateTime.parse(startDate);
        LocalDateTime endDateTime = LocalDateTime.parse(endDate);
        List<Order> orderList = orderRepository.findByCreatedAtBetween(startDateTime,endDateTime)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        return orderList.stream()
                .map(OrderMapper::toOrderDto)
                .toList();
    }

    @Override
    public BigDecimal getTotalSalesForAdmin(String startDate, String endDate, Authentication connectedUser) {
        roleValidator.verifyAdminRole(connectedUser);
        LocalDateTime startDateTime = LocalDateTime.parse(startDate);
        LocalDateTime endDateTime = LocalDateTime.parse(endDate);
        List<Order> orderList = orderRepository.findByCreatedAtBetween(startDateTime,endDateTime)
                .orElseThrow(() -> new OrderNotFoundException("Order not found these between days"));
        return orderList.stream()
                .map(Order::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateTotalPrice(List<CartItem> cartItems) {
         return cartItems.stream()
                .map(item-> item.getProduct().getDiscountedPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);


    }
}
