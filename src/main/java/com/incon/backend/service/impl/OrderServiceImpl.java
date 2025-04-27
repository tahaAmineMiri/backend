package com.incon.backend.service.impl;

import com.incon.backend.dto.request.OrderRequest;
import com.incon.backend.dto.response.OrderResponse;
import com.incon.backend.entity.*;
import com.incon.backend.enums.OrderStatus;
import com.incon.backend.enums.PaymentMethod;
import com.incon.backend.enums.PaymentStatus;
import com.incon.backend.exception.BadRequestException;
import com.incon.backend.exception.ResourceNotFoundException;
import com.incon.backend.mapper.OrderMapper;
import com.incon.backend.repository.*;
import com.incon.backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final BuyerRepository buyerRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final PaymentRepository paymentRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponse createOrder(Integer buyerId, OrderRequest request) {
        System.out.println("Creating order for buyer: " + buyerId);

        Buyer buyer = buyerRepository.findById(buyerId)
                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found with id: " + buyerId));

        Cart cart = buyer.getBuyerCart();
        if (cart == null || cart.getCartItems().isEmpty()) {
            throw new BadRequestException("Cart is empty. Cannot create order.");
        }

        System.out.println("Found cart with items: " + cart.getCartItems().size());

        // Create new order
        Order order = new Order();
        order.setOrderBuyer(buyer);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderShippingAddress(request.getOrderShippingAddress());
        order.setOrderBillingAddress(request.getOrderBillingAddress());

        // Add items from cart to order
        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderItemOrder(order);
            orderItem.setOrderItemProduct(cartItem.getCartItemProduct());
            orderItem.setOrderItemQuantity(cartItem.getCartItemQuantity());
            orderItem.setOrderItemPrice(cartItem.getCartItemPrice());
            orderItem.setOrderItemSubtotal(cartItem.getCartItemSubtotal());
            order.getOrderItems().add(orderItem);
        }

        // Calculate total amount
        order.updateTotalAmount();

        System.out.println("Order total amount: " + order.getOrderTotalAmount());

        // Save order
        Order savedOrder = orderRepository.save(order);

        System.out.println("Saved order with ID: " + savedOrder.getOrderId());
        System.out.println("Order items count: " + savedOrder.getOrderItems().size());

        // Create payment
        Payment payment = new Payment();
        payment.setPaymentOrder(savedOrder);
        payment.setPaymentAmount(savedOrder.getOrderTotalAmount());
        payment.setPaymentMethod(PaymentMethod.BANK_TRANSFER); // Default method
        payment.setPaymentStatus(PaymentStatus.PENDING);
        paymentRepository.save(payment);

        // Clear cart
        cartItemRepository.deleteAll(cart.getCartItems());
        cart.getCartItems().clear();
        cart.updateTotalAmount();
        cartRepository.save(cart);

        OrderResponse response = orderMapper.toResponse(savedOrder);
        System.out.println("Created response with order ID: " +
                (response != null ? response.getOrderId() : "null"));

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByBuyerId(Integer buyerId) {
        Buyer buyer = buyerRepository.findById(buyerId)
                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found with id: " + buyerId));

        List<Order> orders = orderRepository.findByOrderBuyer(buyer);
        return orders.stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByStatus(OrderStatus orderStatus) {
        List<Order> orders = orderRepository.findByOrderStatus(orderStatus);
        return orders.stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Integer orderId, OrderStatus orderStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        // Don't allow changing status if already canceled
        if (order.getOrderStatus() == OrderStatus.CANCELLED) {
            throw new BadRequestException("Cannot change status of a cancelled order");
        }

        order.setOrderStatus(orderStatus);
        Order updatedOrder = orderRepository.save(order);

        return orderMapper.toResponse(updatedOrder);
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        // Only pending or processing orders can be cancelled
        if (order.getOrderStatus() != OrderStatus.PENDING && order.getOrderStatus() != OrderStatus.PROCESSING) {
            throw new BadRequestException("Cannot cancel order with status: " + order.getOrderStatus());
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        Order cancelledOrder = orderRepository.save(order);

        // Update payment status
        if (order.getOrderPayment() != null) {
            Payment payment = order.getOrderPayment();
            payment.setPaymentStatus(PaymentStatus.REJECTED);
            paymentRepository.save(payment);
        }

        return orderMapper.toResponse(cancelledOrder);
    }
}
