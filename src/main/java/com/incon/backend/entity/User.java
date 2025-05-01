package com.incon.backend.entity;

import com.incon.backend.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(unique = true, nullable = false)
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String userEmail;

    @Column(nullable = false)
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @NotBlank(message = "Password is required")
    private String userPassword;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Role is required")
    private Role userRole;

    @NotBlank(message = "Full name is required")
    private String userFullName;

    @NotBlank(message = "Position is required")
    private String userPosition;

    @NotBlank(message = "Business phone is required")
    private String userBusinessPhone;

    @NotNull
    // Add @Builder.Default to fields with initializers
    @Builder.Default
    private boolean userIsVerified = false;

    @CreationTimestamp
    private LocalDateTime userCreatedAt;

    @UpdateTimestamp
    private LocalDateTime userUpdatedAt;

    @OneToOne(mappedBy = "companyUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private Company userCompany;

    @OneToOne(mappedBy = "subscriptionUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private Subscription userSubscription;

    @OneToMany(mappedBy = "reviewUser", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Review> userReviews = new ArrayList<>();

    @OneToMany(mappedBy = "notificationUser", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Notification> userNotifications = new ArrayList<>();

    // Business methods
    public void addReview(Review review) {
        userReviews.add(review);
        review.setReviewUser(this);
    }

    public void removeReview(Review review) {
        userReviews.remove(review);
        review.setReviewUser(null);
    }

    public void assignCompany(Company company) {
        this.userCompany = company;
        if (company != null) {
            company.setCompanyUser(this);
        }
    }

    public void assignSubscription(Subscription subscription) {
        this.userSubscription = subscription;
        if (subscription != null) {
            subscription.setSubscriptionUser(this);
        }
    }

    public void addNotification(Notification notification) {
        userNotifications.add(notification);
        notification.setNotificationUser(this);
    }

    public void removeNotification(Notification notification) {
        userNotifications.remove(notification);
        notification.setNotificationUser(null);
    }
}