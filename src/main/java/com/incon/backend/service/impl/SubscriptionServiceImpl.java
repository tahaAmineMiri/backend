package com.incon.backend.service.impl;

import com.incon.backend.dto.request.SubscriptionRequest;
import com.incon.backend.dto.response.SubscriptionResponse;
import com.incon.backend.entity.Subscription;
import com.incon.backend.entity.User;
import com.incon.backend.enums.SubscriptionType;
import com.incon.backend.exception.BadRequestException;
import com.incon.backend.exception.ResourceNotFoundException;
import com.incon.backend.mapper.SubscriptionMapper;
import com.incon.backend.repository.SubscriptionRepository;
import com.incon.backend.repository.UserRepository;
import com.incon.backend.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final SubscriptionMapper subscriptionMapper;

    @Override
    @Transactional
    public SubscriptionResponse createSubscription(Integer userId, SubscriptionRequest subscriptionRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Check if user already has an active subscription
        subscriptionRepository.findBySubscriptionUser(user)
                .ifPresent(existing -> {
                    if (existing.isSubscriptionIsActive()) {
                        throw new BadRequestException("User already has an active subscription");
                    }
                });

        // Create new subscription
        Subscription subscription = subscriptionMapper.toSubscription(subscriptionRequest);
        subscription.setSubscriptionUser(user);

        // Set default start date if not provided
        if (subscription.getSubscriptionStartDate() == null) {
            subscription.setSubscriptionStartDate(LocalDate.now());
        }

        // Set default end date based on subscription type if not provided
        if (subscription.getSubscriptionEndDate() == null) {
            setDefaultEndDate(subscription);
        }

        // Set default commission rate for COMMISSION_BASED type if not provided
        if (subscription.getSubscriptionType() == SubscriptionType.COMMISSION_BASED &&
                (subscription.getSubscriptionCommissionRate() == null ||
                        subscription.getSubscriptionCommissionRate().compareTo(BigDecimal.ZERO) == 0)) {
            subscription.setSubscriptionCommissionRate(new BigDecimal("5.00")); // Default 5%
        }

        // Save subscription
        Subscription savedSubscription = subscriptionRepository.save(subscription);

        // Update user's subscription reference
        user.setUserSubscription(savedSubscription);
        userRepository.save(user);

        return subscriptionMapper.toSubscriptionResponse(savedSubscription);
    }

    @Override
    @Transactional(readOnly = true)
    public SubscriptionResponse getSubscriptionById(Integer subscriptionId) {
        Subscription subscription = getSubscriptionEntityById(subscriptionId);
        return subscriptionMapper.toSubscriptionResponse(subscription);
    }

    @Override
    @Transactional(readOnly = true)
    public SubscriptionResponse getSubscriptionByUserId(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Subscription subscription = subscriptionRepository.findBySubscriptionUser_UserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found for user with id: " + userId));

        return subscriptionMapper.toSubscriptionResponse(subscription);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionResponse> getAllSubscriptions() {
        return subscriptionRepository.findAll().stream()
                .map(subscriptionMapper::toSubscriptionResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionResponse> getSubscriptionsByType(SubscriptionType type) {
        return subscriptionRepository.findBySubscriptionType(type).stream()
                .map(subscriptionMapper::toSubscriptionResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionResponse> getSubscriptionsByActiveStatus(boolean isActive) {
        return subscriptionRepository.findBySubscriptionIsActive(isActive).stream()
                .map(subscriptionMapper::toSubscriptionResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionResponse> getSubscriptionsExpiringBetween(LocalDate startDate, LocalDate endDate) {
        return subscriptionRepository.findBySubscriptionEndDateBetween(startDate, endDate).stream()
                .map(subscriptionMapper::toSubscriptionResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SubscriptionResponse updateSubscription(Integer subscriptionId, SubscriptionRequest subscriptionRequest) {
        Subscription subscription = getSubscriptionEntityById(subscriptionId);

        // Update subscription fields
        subscriptionMapper.updateSubscriptionFromRequest(subscriptionRequest, subscription);

        // Set default end date based on subscription type if not provided
        if (subscription.getSubscriptionEndDate() == null) {
            setDefaultEndDate(subscription);
        }

        // Save updated subscription
        Subscription updatedSubscription = subscriptionRepository.save(subscription);

        return subscriptionMapper.toSubscriptionResponse(updatedSubscription);
    }

    @Override
    @Transactional
    public SubscriptionResponse renewSubscription(Integer subscriptionId, LocalDate newEndDate) {
        Subscription subscription = getSubscriptionEntityById(subscriptionId);

        if (newEndDate == null || newEndDate.isBefore(LocalDate.now())) {
            throw new BadRequestException("New end date must be in the future");
        }

        subscription.renewSubscription(newEndDate);
        Subscription renewedSubscription = subscriptionRepository.save(subscription);

        return subscriptionMapper.toSubscriptionResponse(renewedSubscription);
    }

    @Override
    @Transactional
    public SubscriptionResponse cancelSubscription(Integer subscriptionId) {
        Subscription subscription = getSubscriptionEntityById(subscriptionId);

        subscription.cancelSubscription();
        Subscription cancelledSubscription = subscriptionRepository.save(subscription);

        return subscriptionMapper.toSubscriptionResponse(cancelledSubscription);
    }

    @Override
    @Transactional
    public void deleteSubscription(Integer subscriptionId) {
        Subscription subscription = getSubscriptionEntityById(subscriptionId);

        // Remove reference from user if exists
        if (subscription.getSubscriptionUser() != null) {
            User user = subscription.getSubscriptionUser();
            user.setUserSubscription(null);
            userRepository.save(user);
        }

        subscriptionRepository.delete(subscription);
    }

    @Override
    @Transactional(readOnly = true)
    public Subscription getSubscriptionEntityById(Integer subscriptionId) {
        return subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found with id: " + subscriptionId));
    }

    /**
     * Sets the default end date based on subscription type
     */
    private void setDefaultEndDate(Subscription subscription) {
        LocalDate startDate = subscription.getSubscriptionStartDate();
        if (startDate == null) {
            startDate = LocalDate.now();
            subscription.setSubscriptionStartDate(startDate);
        }

        switch (subscription.getSubscriptionType()) {
            case MONTHLY:
                subscription.setSubscriptionEndDate(startDate.plusMonths(1));
                break;
            case YEARLY:
                subscription.setSubscriptionEndDate(startDate.plusYears(1));
                break;
            case COMMISSION_BASED:
                // Commission-based subscriptions don't have an end date
                subscription.setSubscriptionEndDate(null);
                break;
            default:
                // Default to one month
                subscription.setSubscriptionEndDate(startDate.plusMonths(1));
        }
    }
}