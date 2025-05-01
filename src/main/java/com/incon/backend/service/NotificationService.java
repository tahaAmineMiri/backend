package com.incon.backend.service;

import com.incon.backend.dto.response.NotificationResponse;
import com.incon.backend.entity.Company;
import com.incon.backend.entity.Subscription;
import com.incon.backend.entity.User;

import java.util.List;

public interface NotificationService {
    /**
     * Send a notification to a user
     *
     * @param userId The ID of the user to notify
     * @param subject The notification subject
     * @param message The notification message
     * @return The notification response with status
     */
    NotificationResponse sendUserNotification(Integer userId, String subject, String message);

    /**
     * Send a verification status notification to a company
     *
     * @param company The company whose verification status changed
     * @param isVerified Whether the company was verified or rejected
     * @return The notification response with status
     */
    NotificationResponse sendCompanyVerificationNotification(Company company, boolean isVerified);

    /**
     * Send a subscription expiration reminder
     *
     * @param subscription The subscription that is expiring
     * @param daysRemaining The number of days remaining until expiration
     * @return The notification response with status
     */
    NotificationResponse sendSubscriptionExpirationReminder(Subscription subscription, int daysRemaining);

    /**
     * Send a welcome notification to a new user
     *
     * @param user The new user
     * @return The notification response with status
     */
    NotificationResponse sendWelcomeNotification(User user);

    /**
     * Send a bulk notification to multiple users
     *
     * @param userIds The list of user IDs to notify
     * @param subject The notification subject
     * @param message The notification message
     * @return The list of notification responses with statuses
     */
    List<NotificationResponse> sendBulkNotification(List<Integer> userIds, String subject, String message);

    /**
     * Get all notifications for a user
     *
     * @param userId The user ID
     * @return List of notification responses
     */
    List<NotificationResponse> getUserNotifications(Integer userId);

    /**
     * Mark a notification as read
     *
     * @param notificationId The notification ID
     * @return The updated notification response
     */
    NotificationResponse markNotificationAsRead(Integer notificationId);
}