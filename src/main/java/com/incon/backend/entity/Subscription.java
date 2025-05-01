package com.incon.backend.entity;

import com.incon.backend.enums.SubscriptionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer subscriptionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionType subscriptionType;

    @Column(nullable = false)
    private LocalDate subscriptionStartDate;

    private LocalDate subscriptionEndDate;

    @Builder.Default
    private boolean subscriptionIsActive = true;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subscriptionAmount;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal subscriptionCommissionRate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User subscriptionUser;

    @CreationTimestamp
    private LocalDateTime subscriptionCreatedAt;

    @UpdateTimestamp
    private LocalDateTime subscriptionUpdatedAt;

    // Business methods
    public void renewSubscription(LocalDate newEndDate) {
        this.subscriptionEndDate = newEndDate;
        this.subscriptionIsActive = true;
    }

    public void cancelSubscription() {
        this.subscriptionIsActive = false;
    }
}