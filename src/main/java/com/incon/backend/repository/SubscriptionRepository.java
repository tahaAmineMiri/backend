package com.incon.backend.repository;

import com.incon.backend.entity.Subscription;
import com.incon.backend.entity.User;
import com.incon.backend.enums.SubscriptionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {
    Optional<Subscription> findBySubscriptionUser(User user);

    // Fix this method name
    Optional<Subscription> findBySubscriptionUser_UserId(Integer userId);

    List<Subscription> findBySubscriptionType(SubscriptionType type);
    List<Subscription> findBySubscriptionIsActive(boolean isActive);
    List<Subscription> findBySubscriptionEndDateBefore(LocalDate date);
    List<Subscription> findBySubscriptionEndDateBetween(LocalDate startDate, LocalDate endDate);
}