package com.incon.backend.controller;

import com.incon.backend.dto.request.AdminRequest;
import com.incon.backend.dto.response.*;
import com.incon.backend.enums.OrderStatus;
import com.incon.backend.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // New company management endpoints
    @GetMapping("/companies")
    public ResponseEntity<List<CompanyResponse>> getAllCompanies() {
        List<CompanyResponse> responses = adminService.getAllCompanies();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/companies/pending")
    public ResponseEntity<List<CompanyResponse>> getPendingVerificationCompanies() {
        List<CompanyResponse> responses = adminService.getPendingVerificationCompanies();
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/companies/{companyId}/verify")
    public ResponseEntity<CompanyResponse> verifyCompany(@PathVariable Integer companyId) {
        CompanyResponse response = adminService.verifyCompany(companyId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/companies/{companyId}/reject")
    public ResponseEntity<CompanyResponse> rejectCompany(@PathVariable Integer companyId) {
        CompanyResponse response = adminService.rejectCompany(companyId);
        return ResponseEntity.ok(response);
    }

    // New subscription management endpoints
    @GetMapping("/subscriptions")
    public ResponseEntity<List<SubscriptionResponse>> getAllSubscriptions() {
        List<SubscriptionResponse> responses = adminService.getAllSubscriptions();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/subscriptions/expiring")
    public ResponseEntity<List<SubscriptionResponse>> getExpiringSubscriptions(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate withinDays) {
        List<SubscriptionResponse> responses = adminService.getExpiringSubscriptions(withinDays);
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/subscriptions/{subscriptionId}/activate")
    public ResponseEntity<SubscriptionResponse> activateSubscription(@PathVariable Integer subscriptionId) {
        SubscriptionResponse response = adminService.activateSubscription(subscriptionId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/subscriptions/{subscriptionId}/deactivate")
    public ResponseEntity<SubscriptionResponse> deactivateSubscription(@PathVariable Integer subscriptionId) {
        SubscriptionResponse response = adminService.deactivateSubscription(subscriptionId);
        return ResponseEntity.ok(response);
    }

    // Dashboard analytics endpoint
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalActiveUsers", adminService.getTotalActiveUsers());
        stats.put("totalVerifiedCompanies", adminService.getTotalVerifiedCompanies());
        stats.put("totalActiveSubscriptions", adminService.getTotalActiveSubscriptions());
        stats.put("totalPendingVerifications", adminService.getTotalPendingVerifications());

        return ResponseEntity.ok(stats);
    }
}