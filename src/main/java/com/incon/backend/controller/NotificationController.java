package com.incon.backend.controller;

import com.incon.backend.dto.response.ApiResponse;
import com.incon.backend.dto.response.NotificationResponse;
import com.incon.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/user/{userId}")
    public ResponseEntity<NotificationResponse> sendUserNotification(
            @PathVariable Integer userId,
            @RequestBody Map<String, String> notificationData) {

        String subject = notificationData.get("subject");
        String message = notificationData.get("message");

        if (subject == null || message == null) {
            return ResponseEntity.badRequest().build();
        }

        NotificationResponse response = notificationService.sendUserNotification(userId, subject, message);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<NotificationResponse>> sendBulkNotification(
            @RequestBody Map<String, Object> bulkNotificationData) {

        @SuppressWarnings("unchecked")
        List<Integer> userIds = (List<Integer>) bulkNotificationData.get("userIds");
        String subject = (String) bulkNotificationData.get("subject");
        String message = (String) bulkNotificationData.get("message");

        if (userIds == null || subject == null || message == null) {
            return ResponseEntity.badRequest().build();
        }

        List<NotificationResponse> responses = notificationService.sendBulkNotification(userIds, subject, message);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponse>> getUserNotifications(@PathVariable Integer userId) {
        List<NotificationResponse> notifications = notificationService.getUserNotifications(userId);
        return ResponseEntity.ok(notifications);
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<NotificationResponse> markNotificationAsRead(@PathVariable Integer notificationId) {
        NotificationResponse response = notificationService.markNotificationAsRead(notificationId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/user/{userId}/read-all")
    public ResponseEntity<ApiResponse> markAllNotificationsAsRead(@PathVariable Integer userId) {
        List<NotificationResponse> notifications = notificationService.getUserNotifications(userId);

        for (NotificationResponse notification : notifications) {
            if (!notification.isRead()) {
                notificationService.markNotificationAsRead(notification.getNotificationId());
            }
        }

        return ResponseEntity.ok(new ApiResponse("All notifications marked as read", HttpStatus.OK));
    }
}