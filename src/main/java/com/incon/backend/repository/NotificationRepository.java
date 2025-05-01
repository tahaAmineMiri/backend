package com.incon.backend.repository;

import com.incon.backend.entity.Notification;
import com.incon.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByNotificationUser(User user);

    // Fix this method name
    List<Notification> findByNotificationUser_UserId(Integer userId);

    // Fix this method name
    List<Notification> findByNotificationUser_UserIdAndNotificationIsRead(Integer userId, boolean isRead);

    List<Notification> findByNotificationCreatedAtAfter(LocalDateTime dateTime);
    List<Notification> findByNotificationType(String type);
}