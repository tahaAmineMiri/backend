package com.incon.backend.service.impl;

import com.incon.backend.dto.response.NotificationResponse;
import com.incon.backend.entity.Company;
import com.incon.backend.entity.Notification;
import com.incon.backend.entity.Subscription;
import com.incon.backend.entity.User;
import com.incon.backend.exception.ResourceNotFoundException;
import com.incon.backend.mapper.NotificationMapper;
import com.incon.backend.repository.NotificationRepository;
import com.incon.backend.repository.UserRepository;
import com.incon.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;

    @Override
    @Transactional
    public NotificationResponse sendUserNotification(Integer userId, String subject, String message) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Notification notification = Notification.builder()
                .notificationUser(user)
                .notificationSubject(subject)
                .notificationMessage(message)
                .notificationType("USER_NOTIFICATION")
                .notificationDeliveryStatus(true)
                .notificationDeliveryMessage("Notification delivered successfully")
                .build();

        Notification savedNotification = notificationRepository.save(notification);
        return notificationMapper.toNotificationResponse(savedNotification);
    }

    @Override
    @Transactional
    public NotificationResponse sendCompanyVerificationNotification(Company company, boolean isVerified) {
        if (company == null || company.getCompanyUser() == null) {
            throw new IllegalArgumentException("Company or company user cannot be null");
        }

        User user = company.getCompanyUser();
        String subject = isVerified ?
                "Company Verification Approved" :
                "Company Verification Rejected";

        String message = isVerified ?
                "Congratulations! Your company " + company.getCompanyName() + " has been verified successfully." :
                "We regret to inform you that your company " + company.getCompanyName() + " verification has been rejected.";

        Notification notification = Notification.builder()
                .notificationUser(user)
                .notificationSubject(subject)
                .notificationMessage(message)
                .notificationType("COMPANY_VERIFICATION")
                .notificationDeliveryStatus(true)
                .notificationDeliveryMessage("Notification delivered successfully")
                .build();

        Notification savedNotification = notificationRepository.save(notification);
        return notificationMapper.toNotificationResponse(savedNotification);
    }

    @Override
    @Transactional
    public NotificationResponse sendSubscriptionExpirationReminder(Subscription subscription, int daysRemaining) {
        if (subscription == null || subscription.getSubscriptionUser() == null) {
            throw new IllegalArgumentException("Subscription or subscription user cannot be null");
        }

        User user = subscription.getSubscriptionUser();
        String subject = "Subscription Expiration Reminder";
        String message = "Your subscription will expire in " + daysRemaining + " days. " +
                "Please renew your subscription to continue enjoying our services without interruption.";

        Notification notification = Notification.builder()
                .notificationUser(user)
                .notificationSubject(subject)
                .notificationMessage(message)
                .notificationType("SUBSCRIPTION_EXPIRATION")
                .notificationDeliveryStatus(true)
                .notificationDeliveryMessage("Notification delivered successfully")
                .build();

        Notification savedNotification = notificationRepository.save(notification);
        return notificationMapper.toNotificationResponse(savedNotification);
    }

    @Override
    @Transactional
    public NotificationResponse sendWelcomeNotification(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        String subject = "Welcome to Incon Marketplace";
        String message = "Welcome, " + user.getUserFullName() + "! " +
                "Thank you for joining Incon Marketplace. We're excited to have you on board. " +
                "If you have any questions, please don't hesitate to contact our support team.";

        Notification notification = Notification.builder()
                .notificationUser(user)
                .notificationSubject(subject)
                .notificationMessage(message)
                .notificationType("WELCOME")
                .notificationDeliveryStatus(true)
                .notificationDeliveryMessage("Notification delivered successfully")
                .build();

        Notification savedNotification = notificationRepository.save(notification);
        return notificationMapper.toNotificationResponse(savedNotification);
    }

    @Override
    @Transactional
    public List<NotificationResponse> sendBulkNotification(List<Integer> userIds, String subject, String message) {
        List<NotificationResponse> responses = new ArrayList<>();

        for (Integer userId : userIds) {
            try {
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

                Notification notification = Notification.builder()
                        .notificationUser(user)
                        .notificationSubject(subject)
                        .notificationMessage(message)
                        .notificationType("BULK_NOTIFICATION")
                        .notificationDeliveryStatus(true)
                        .notificationDeliveryMessage("Notification delivered successfully")
                        .build();

                Notification savedNotification = notificationRepository.save(notification);
                responses.add(notificationMapper.toNotificationResponse(savedNotification));
            } catch (Exception e) {
                // Create a failed notification response without saving to the database
                NotificationResponse failedResponse = NotificationResponse.builder()
                        .userId(userId)
                        .subject(subject)
                        .message(message)
                        .deliveryStatus(false)
                        .deliveryMessage("Failed to deliver: " + e.getMessage())
                        .build();
                responses.add(failedResponse);
            }
        }

        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getUserNotifications(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        List<Notification> notifications = notificationRepository.findByNotificationUser_UserId(userId);
        return notifications.stream()
                .map(notificationMapper::toNotificationResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public NotificationResponse markNotificationAsRead(Integer notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + notificationId));

        notification.setNotificationIsRead(true);
        Notification updatedNotification = notificationRepository.save(notification);
        return notificationMapper.toNotificationResponse(updatedNotification);
    }
}