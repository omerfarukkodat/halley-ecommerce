package com.kodat.of.halleyecommerce.order;

import com.kodat.of.halleyecommerce.address.Address;
import com.kodat.of.halleyecommerce.address.AddressRepository;
import com.kodat.of.halleyecommerce.cart.Cart;
import com.kodat.of.halleyecommerce.cart.CartItem;
import com.kodat.of.halleyecommerce.dto.order.OrderDto;
import com.kodat.of.halleyecommerce.exception.AddressNotFoundException;
import com.kodat.of.halleyecommerce.exception.OrderNotFoundException;
import com.kodat.of.halleyecommerce.mapper.order.OrderMapper;
import com.kodat.of.halleyecommerce.user.CustomUserDetails;
import com.kodat.of.halleyecommerce.user.User;
import com.kodat.of.halleyecommerce.validator.CartValidator;
import com.kodat.of.halleyecommerce.validator.RoleValidator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final RoleValidator roleValidator;
    private final CartValidator cartValidator;
    private final AddressRepository addressRepository;

    public OrderServiceImpl(OrderRepository orderRepository, RoleValidator roleValidator, CartValidator cartValidator, AddressRepository addressRepository) {
        this.orderRepository = orderRepository;
        this.roleValidator = roleValidator;
        this.cartValidator = cartValidator;
        this.addressRepository = addressRepository;
    }


    @Override
    public OrderDto createOrderFromCart(OrderDto orderDto, Authentication connectedUser) {
       roleValidator.verifyUserRole(connectedUser);
        CustomUserDetails customUserDetails = (CustomUserDetails) connectedUser.getPrincipal();
        User user = customUserDetails.getUser();
        Cart cart = cartValidator.validateCartAndUser(connectedUser);
        Address address = addressRepository.findById(orderDto.getAddressId())
                .orElseThrow(() -> new AddressNotFoundException("Address not found"));
        Order order = Order.builder()
                .user(user)
                .totalPrice(calculateTotalPrice(cart.getItems()))
                .addressId(address.getId())
                .status("PENDING")
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

    private BigDecimal calculateTotalPrice(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(item-> item.getProduct().getDiscountedPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }
}
