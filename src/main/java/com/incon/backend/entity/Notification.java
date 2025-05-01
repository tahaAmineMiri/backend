package com.incon.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer notificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User notificationUser;

    @Column(nullable = false)
    private String notificationSubject;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String notificationMessage;

    @Builder.Default
    private boolean notificationIsRead = false;

    private String notificationType;

    @CreationTimestamp
    private LocalDateTime notificationCreatedAt;

    @Builder.Default
    private boolean notificationDeliveryStatus = false;

    private String notificationDeliveryMessage;
}