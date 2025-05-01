package com.incon.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private Integer notificationId;
    private Integer userId;
    private String subject;
    private String message;
    private boolean isRead;
    private String notificationType;
    private LocalDateTime createdAt;
    private boolean deliveryStatus;
    private String deliveryMessage;
}