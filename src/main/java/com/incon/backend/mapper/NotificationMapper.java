package com.incon.backend.mapper;

import com.incon.backend.dto.response.NotificationResponse;
import com.incon.backend.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(source = "notificationId", target = "notificationId")
    @Mapping(source = "notificationUser.userId", target = "userId")
    @Mapping(source = "notificationSubject", target = "subject")
    @Mapping(source = "notificationMessage", target = "message")
    @Mapping(source = "notificationIsRead", target = "isRead")
    @Mapping(source = "notificationType", target = "notificationType")
    @Mapping(source = "notificationCreatedAt", target = "createdAt")
    @Mapping(source = "notificationDeliveryStatus", target = "deliveryStatus")
    @Mapping(source = "notificationDeliveryMessage", target = "deliveryMessage")
    NotificationResponse toNotificationResponse(Notification notification);
}