package com.incon.backend.service;

import com.incon.backend.dto.request.AdminRequest;
import com.incon.backend.dto.response.AdminResponse;
import com.incon.backend.dto.response.OrderResponse;
import com.incon.backend.dto.response.PaymentResponse;
import com.incon.backend.enums.OrderStatus;

import java.util.List;

public interface AdminService {
    // Admin management
    AdminResponse createAdmin(AdminRequest adminRequest);
    AdminResponse getAdminById(Integer adminId);
    List<AdminResponse> getAllAdmins();
    AdminResponse updateAdmin(Integer adminId, AdminRequest adminRequest);
    void deleteAdmin(Integer adminId);

    // Order management
    List<OrderResponse> getAllOrders();
    OrderResponse updateOrderStatus(Integer orderId, OrderStatus status);

    // Payment verification
    PaymentResponse verifyPayment(Integer paymentId);
    PaymentResponse rejectPayment(Integer paymentId);
    List<PaymentResponse> getPendingPayments();

    // User management
    void verifyUser(Integer userId);
    void unverifyUser(Integer userId);
}