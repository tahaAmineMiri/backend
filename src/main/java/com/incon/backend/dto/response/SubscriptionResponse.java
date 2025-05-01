package com.incon.backend.dto.response;

import com.incon.backend.enums.SubscriptionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionResponse {
    private Integer subscriptionId;
    private SubscriptionType subscriptionType;
    private LocalDate subscriptionStartDate;
    private LocalDate subscriptionEndDate;
    private boolean subscriptionIsActive;
    private BigDecimal subscriptionAmount;
    private BigDecimal subscriptionCommissionRate;
    private Integer userId;
    private LocalDateTime subscriptionCreatedAt;
}