package com.incon.backend.dto.request;

import com.incon.backend.enums.SubscriptionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionRequest {

    @NotNull(message = "Subscription type is required")
    private SubscriptionType subscriptionType;

    private LocalDate subscriptionStartDate;

    private LocalDate subscriptionEndDate;

    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero")
    private BigDecimal subscriptionAmount;

    @DecimalMin(value = "0.0", inclusive = true, message = "Commission rate cannot be negative")
    private BigDecimal subscriptionCommissionRate;
}