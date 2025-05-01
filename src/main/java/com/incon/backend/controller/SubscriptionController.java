package com.incon.backend.controller;

import com.incon.backend.dto.request.SubscriptionRequest;
import com.incon.backend.dto.response.ApiResponse;
import com.incon.backend.dto.response.SubscriptionResponse;
import com.incon.backend.enums.SubscriptionType;
import com.incon.backend.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/user/{userId}")
    public ResponseEntity<SubscriptionResponse> createSubscription(
            @PathVariable Integer userId,
            @Valid @RequestBody SubscriptionRequest subscriptionRequest) {
        SubscriptionResponse response = subscriptionService.createSubscription(userId, subscriptionRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{subscriptionId}")
    public ResponseEntity<SubscriptionResponse> getSubscriptionById(@PathVariable Integer subscriptionId) {
        SubscriptionResponse response = subscriptionService.getSubscriptionById(subscriptionId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<SubscriptionResponse> getSubscriptionByUserId(@PathVariable Integer userId) {
        SubscriptionResponse response = subscriptionService.getSubscriptionByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionResponse>> getAllSubscriptions() {
        List<SubscriptionResponse> responses = subscriptionService.getAllSubscriptions();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<SubscriptionResponse>> getSubscriptionsByType(
            @PathVariable SubscriptionType type) {
        List<SubscriptionResponse> responses = subscriptionService.getSubscriptionsByType(type);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/active/{isActive}")
    public ResponseEntity<List<SubscriptionResponse>> getSubscriptionsByActiveStatus(
            @PathVariable boolean isActive) {
        List<SubscriptionResponse> responses = subscriptionService.getSubscriptionsByActiveStatus(isActive);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/expiring")
    public ResponseEntity<List<SubscriptionResponse>> getSubscriptionsExpiringBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<SubscriptionResponse> responses = subscriptionService.getSubscriptionsExpiringBetween(startDate, endDate);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{subscriptionId}")
    public ResponseEntity<SubscriptionResponse> updateSubscription(
            @PathVariable Integer subscriptionId,
            @Valid @RequestBody SubscriptionRequest subscriptionRequest) {
        SubscriptionResponse response = subscriptionService.updateSubscription(subscriptionId, subscriptionRequest);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{subscriptionId}/renew")
    public ResponseEntity<SubscriptionResponse> renewSubscription(
            @PathVariable Integer subscriptionId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate newEndDate) {
        SubscriptionResponse response = subscriptionService.renewSubscription(subscriptionId, newEndDate);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{subscriptionId}/cancel")
    public ResponseEntity<SubscriptionResponse> cancelSubscription(@PathVariable Integer subscriptionId) {
        SubscriptionResponse response = subscriptionService.cancelSubscription(subscriptionId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{subscriptionId}")
    public ResponseEntity<ApiResponse> deleteSubscription(@PathVariable Integer subscriptionId) {
        subscriptionService.deleteSubscription(subscriptionId);
        return ResponseEntity.ok(new ApiResponse("Subscription deleted successfully", HttpStatus.OK));
    }
}