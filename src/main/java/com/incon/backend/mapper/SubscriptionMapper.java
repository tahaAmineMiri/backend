package com.incon.backend.mapper;

import com.incon.backend.dto.request.SubscriptionRequest;
import com.incon.backend.dto.response.SubscriptionResponse;
import com.incon.backend.entity.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    @Mapping(target = "subscriptionId", ignore = true)
    @Mapping(target = "subscriptionIsActive", constant = "true")
    @Mapping(target = "subscriptionUser", ignore = true)
    @Mapping(target = "subscriptionCreatedAt", ignore = true)
    @Mapping(target = "subscriptionUpdatedAt", ignore = true)
    Subscription toSubscription(SubscriptionRequest subscriptionRequest);

    @Mapping(source = "subscriptionUser.userId", target = "userId")
    SubscriptionResponse toSubscriptionResponse(Subscription subscription);

    @Mapping(target = "subscriptionId", ignore = true)
    @Mapping(target = "subscriptionUser", ignore = true)
    @Mapping(target = "subscriptionCreatedAt", ignore = true)
    @Mapping(target = "subscriptionUpdatedAt", ignore = true)
    void updateSubscriptionFromRequest(SubscriptionRequest subscriptionRequest, @MappingTarget Subscription subscription);
}