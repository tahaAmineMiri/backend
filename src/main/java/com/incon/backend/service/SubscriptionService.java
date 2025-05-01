package com.incon.backend.service;

import com.incon.backend.dto.request.SubscriptionRequest;
import com.incon.backend.dto.response.SubscriptionResponse;
import com.incon.backend.entity.Subscription;
import com.incon.backend.enums.SubscriptionType;

import java.time.LocalDate;
import java.util.List;

public interface SubscriptionService {
    /**
     * Create a new subscription for a user
     *
     * @param userId The user ID
     * @param subscriptionRequest The subscription details
     * @return The subscription response
     */
    SubscriptionResponse createSubscription(Integer userId, SubscriptionRequest subscriptionRequest);

    /**
     * Get a subscription by its ID
     *
     * @param subscriptionId The subscription ID
     * @return The subscription response
     */
    SubscriptionResponse getSubscriptionById(Integer subscriptionId);

    /**
     * Get a subscription by its associated user ID
     *
     * @param userId The user ID
     * @return The subscription response
     */
    SubscriptionResponse getSubscriptionByUserId(Integer userId);

    /**
     * Get all subscriptions
     *
     * @return List of subscription responses
     */
    List<SubscriptionResponse> getAllSubscriptions();

    /**
     * Get all subscriptions of a specific type
     *
     * @param type The subscription type
     * @return List of subscription responses
     */
    List<SubscriptionResponse> getSubscriptionsByType(SubscriptionType type);

    /**
     * Get all active/inactive subscriptions
     *
     * @param isActive The active status
     * @return List of subscription responses
     */
    List<SubscriptionResponse> getSubscriptionsByActiveStatus(boolean isActive);

    /**
     * Get all subscriptions that will expire within a date range
     *
     * @param startDate The start date
     * @param endDate The end date
     * @return List of subscription responses
     */
    List<SubscriptionResponse> getSubscriptionsExpiringBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Update a subscription
     *
     * @param subscriptionId The subscription ID
     * @param subscriptionRequest The updated subscription details
     * @return The updated subscription response
     */
    SubscriptionResponse updateSubscription(Integer subscriptionId, SubscriptionRequest subscriptionRequest);

    /**
     * Renew a subscription with a new end date
     *
     * @param subscriptionId The subscription ID
     * @param newEndDate The new end date
     * @return The updated subscription response
     */
    SubscriptionResponse renewSubscription(Integer subscriptionId, LocalDate newEndDate);

    /**
     * Cancel a subscription
     *
     * @param subscriptionId The subscription ID
     * @return The updated subscription response
     */
    SubscriptionResponse cancelSubscription(Integer subscriptionId);

    /**
     * Delete a subscription
     *
     * @param subscriptionId The subscription ID
     */
    void deleteSubscription(Integer subscriptionId);

    /**
     * Get subscription entity by ID (for internal use)
     *
     * @param subscriptionId The subscription ID
     * @return The subscription entity
     */
    Subscription getSubscriptionEntityById(Integer subscriptionId);
}