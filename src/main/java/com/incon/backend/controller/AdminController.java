package com.incon.backend.controller;

import com.incon.backend.dto.request.AdminRequest;
import com.incon.backend.dto.response.AdminResponse;
import com.incon.backend.dto.response.ApiResponse;
import com.incon.backend.dto.response.OrderResponse;
import com.incon.backend.dto.response.PaymentResponse;
import com.incon.backend.enums.OrderStatus;
import com.incon.backend.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // Admin management endpoints
    @PostMapping
    public ResponseEntity<AdminResponse> createAdmin(@Valid @RequestBody AdminRequest adminRequest) {
        AdminResponse response = adminService.createAdmin(adminRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{adminId}")
    public ResponseEntity<AdminResponse> getAdminById(@PathVariable Integer adminId) {
        AdminResponse response = adminService.getAdminById(adminId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<AdminResponse>> getAllAdmins() {
        List<AdminResponse> responses = adminService.getAllAdmins();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{adminId}")
    public ResponseEntity<AdminResponse> updateAdmin(
            @PathVariable Integer adminId,
            @Valid @RequestBody AdminRequest adminRequest) {
        AdminResponse response = adminService.updateAdmin(adminId, adminRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{adminId}")
    public ResponseEntity<ApiResponse> deleteAdmin(@PathVariable Integer adminId) {
        adminService.deleteAdmin(adminId);
        return ResponseEntity.ok(new ApiResponse("Admin deleted successfully", HttpStatus.OK));
    }

    // Order management endpoints
    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> responses = adminService.getAllOrders();
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/orders/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Integer orderId,
            @RequestParam OrderStatus status) {
        OrderResponse response = adminService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(response);
    }

    // Payment management endpoints
    @GetMapping("/payments/pending")
    public ResponseEntity<List<PaymentResponse>> getPendingPayments() {
        List<PaymentResponse> responses = adminService.getPendingPayments();
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/payments/{paymentId}/verify")
    public ResponseEntity<PaymentResponse> verifyPayment(@PathVariable Integer paymentId) {
        PaymentResponse response = adminService.verifyPayment(paymentId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/payments/{paymentId}/reject")
    public ResponseEntity<PaymentResponse> rejectPayment(@PathVariable Integer paymentId) {
        PaymentResponse response = adminService.rejectPayment(paymentId);
        return ResponseEntity.ok(response);
    }

    // User verification endpoints
    @PatchMapping("/users/{userId}/verify")
    public ResponseEntity<ApiResponse> verifyUser(@PathVariable Integer userId) {
        adminService.verifyUser(userId);
        return ResponseEntity.ok(new ApiResponse("User verified successfully", HttpStatus.OK));
    }

    @PatchMapping("/users/{userId}/unverify")
    public ResponseEntity<ApiResponse> unverifyUser(@PathVariable Integer userId) {
        adminService.unverifyUser(userId);
        return ResponseEntity.ok(new ApiResponse("User unverified successfully", HttpStatus.OK));
    }
}