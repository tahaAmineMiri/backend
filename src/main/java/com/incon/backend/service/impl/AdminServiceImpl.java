package com.incon.backend.service.impl;

import com.incon.backend.dto.request.AdminRequest;
import com.incon.backend.dto.response.AdminResponse;
import com.incon.backend.dto.response.OrderResponse;
import com.incon.backend.dto.response.PaymentResponse;
import com.incon.backend.entity.Admin;
import com.incon.backend.entity.Order;
import com.incon.backend.entity.Payment;
import com.incon.backend.entity.User;
import com.incon.backend.enums.OrderStatus;
import com.incon.backend.enums.PaymentStatus;
import com.incon.backend.exception.BadRequestException;
import com.incon.backend.exception.ResourceNotFoundException;
import com.incon.backend.mapper.AdminMapper;
import com.incon.backend.mapper.OrderMapper;
import com.incon.backend.mapper.PaymentMapper;
import com.incon.backend.repository.AdminRepository;
import com.incon.backend.repository.OrderRepository;
import com.incon.backend.repository.PaymentRepository;
import com.incon.backend.repository.UserRepository;
import com.incon.backend.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final AdminMapper adminMapper;
    private final OrderMapper orderMapper;
    private final PaymentMapper paymentMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public AdminResponse createAdmin(AdminRequest adminRequest) {
        if (adminRepository.existsByAdminEmail(adminRequest.getAdminEmail())) {
            throw new BadRequestException("Email is already registered");
        }

        Admin admin = adminMapper.toAdmin(adminRequest);
        admin.setAdminPassword(passwordEncoder.encode(adminRequest.getAdminPassword()));
        Admin savedAdmin = adminRepository.save(admin);

        return adminMapper.toAdminResponse(savedAdmin);
    }

    @Override
    @Transactional(readOnly = true)
    public AdminResponse getAdminById(Integer adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with id: " + adminId));
        return adminMapper.toAdminResponse(admin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminResponse> getAllAdmins() {
        return adminRepository.findAll().stream()
                .map(adminMapper::toAdminResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AdminResponse updateAdmin(Integer adminId, AdminRequest adminRequest) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with id: " + adminId));

        if (!admin.getAdminEmail().equals(adminRequest.getAdminEmail()) &&
                adminRepository.existsByAdminEmail(adminRequest.getAdminEmail())) {
            throw new BadRequestException("Email is already taken");
        }

        admin.setAdminName(adminRequest.getAdminName());
        admin.setAdminEmail(adminRequest.getAdminEmail());

        if (adminRequest.getAdminPassword() != null && !adminRequest.getAdminPassword().isBlank()) {
            admin.setAdminPassword(passwordEncoder.encode(adminRequest.getAdminPassword()));
        }

        Admin updatedAdmin = adminRepository.save(admin);
        return adminMapper.toAdminResponse(updatedAdmin);
    }

    @Override
    @Transactional
    public void deleteAdmin(Integer adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with id: " + adminId));
        adminRepository.delete(admin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Integer orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        order.setOrderStatus(status);
        Order updatedOrder = orderRepository.save(order);

        return orderMapper.toResponse(updatedOrder);
    }

    @Override
    @Transactional
    public PaymentResponse verifyPayment(Integer paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));

        payment.setPaymentStatus(PaymentStatus.VERIFIED);

        // Also update the corresponding order status
        if (payment.getPaymentOrder() != null) {
            Order order = payment.getPaymentOrder();
            order.setOrderStatus(OrderStatus.PROCESSING);
            orderRepository.save(order);
        }

        Payment verifiedPayment = paymentRepository.save(payment);
        return paymentMapper.toResponse(verifiedPayment);
    }

    @Override
    @Transactional
    public PaymentResponse rejectPayment(Integer paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));

        payment.setPaymentStatus(PaymentStatus.REJECTED);

        // Also update the corresponding order status
        if (payment.getPaymentOrder() != null) {
            Order order = payment.getPaymentOrder();
            order.setOrderStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
        }

        Payment rejectedPayment = paymentRepository.save(payment);
        return paymentMapper.toResponse(rejectedPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getPendingPayments() {
        List<Payment> pendingPayments = paymentRepository.findByPaymentStatus(PaymentStatus.PENDING);
        return pendingPayments.stream()
                .map(paymentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void verifyUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        user.setUserIsVerified(true);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void unverifyUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        user.setUserIsVerified(false);
        userRepository.save(user);
    }
}