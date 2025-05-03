package com.incon.backend.controller;

import com.incon.backend.dto.request.OrderRequest;
import com.incon.backend.dto.response.ApiResponse;
import com.incon.backend.dto.response.OrderResponse;
import com.incon.backend.enums.OrderStatus;
import com.incon.backend.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/buyer/{buyerId}")
    public ResponseEntity<OrderResponse> createOrder(
            @PathVariable Integer buyerId,
            @Valid @RequestBody OrderRequest request) {
        OrderResponse orderResponse = orderService.createOrder(buyerId, request);
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Integer orderId) {
        OrderResponse orderResponse = orderService.getOrderById(orderId);
        return ResponseEntity.ok(orderResponse);
    }

    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByBuyerId(@PathVariable Integer buyerId) {
        List<OrderResponse> orderResponses = orderService.getOrdersByBuyerId(buyerId);
        return ResponseEntity.ok(orderResponses);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderResponse>> getOrdersByStatus(
            @PathVariable OrderStatus status) {
        List<OrderResponse> orderResponses = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(orderResponses);
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Integer orderId,
            @RequestParam OrderStatus status) {
        OrderResponse orderResponse = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(orderResponse);
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Integer orderId) {
        OrderResponse orderResponse = orderService.cancelOrder(orderId);
        return ResponseEntity.ok(orderResponse);
    }
}