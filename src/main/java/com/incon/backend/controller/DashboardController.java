package com.incon.backend.controller;

import com.incon.backend.dto.response.ApiResponse;
import com.incon.backend.service.AdminService;
import com.incon.backend.service.ProductService;
import com.incon.backend.service.OrderService;
import com.incon.backend.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final AdminService adminService;
    private final ProductService productService;
    private final OrderService orderService;

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        // User stats
        stats.put("totalActiveUsers", adminService.getTotalActiveUsers());
        stats.put("totalVerifiedCompanies", adminService.getTotalVerifiedCompanies());

        // Subscription stats
        stats.put("totalActiveSubscriptions", adminService.getTotalActiveSubscriptions());

        // Verification stats
        stats.put("pendingVerifications", adminService.getTotalPendingVerifications());

        // Product stats
        stats.put("totalProducts", productService.getAllProducts().size());

        // Order stats
        stats.put("pendingOrders", orderService.getOrdersByStatus(OrderStatus.PENDING).size());
        stats.put("processingOrders", orderService.getOrdersByStatus(OrderStatus.PROCESSING).size());
        stats.put("shippedOrders", orderService.getOrdersByStatus(OrderStatus.SHIPPED).size());
        stats.put("deliveredOrders", orderService.getOrdersByStatus(OrderStatus.DELIVERED).size());
        stats.put("cancelledOrders", orderService.getOrdersByStatus(OrderStatus.CANCELLED).size());

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/health")
    public ResponseEntity<ApiResponse> getSystemHealth() {
        // In a real implementation, this would check various system components
        return ResponseEntity.ok(new ApiResponse("System is healthy", HttpStatus.OK));
    }

    @GetMapping("/revenue")
    public ResponseEntity<Map<String, Object>> getRevenueStats(
            @RequestParam(required = false, defaultValue = "30") Integer days) {

        Map<String, Object> revenueStats = new HashMap<>();

        // In a real implementation, these would be actual calculations from the database
        revenueStats.put("totalRevenue", 125000.00);
        revenueStats.put("commissionRevenue", 35000.00);
        revenueStats.put("subscriptionRevenue", 90000.00);
        revenueStats.put("period", days + " days");

        Map<String, Double> revenueByCategory = new HashMap<>();
        revenueByCategory.put("Electronics", 45000.00);
        revenueByCategory.put("Fashion", 30000.00);
        revenueByCategory.put("Home & Garden", 25000.00);
        revenueByCategory.put("Books", 15000.00);
        revenueByCategory.put("Other", 10000.00);

        revenueStats.put("revenueByCategory", revenueByCategory);

        return ResponseEntity.ok(revenueStats);
    }
}