package com.incon.backend.util;

import java.math.BigDecimal;

/**
 * Constants for subscription related operations
 */
public class SubscriptionConstants {
    // Monthly subscription fee
    public static final BigDecimal MONTHLY_SUBSCRIPTION_FEE = new BigDecimal("15.00");

    // Yearly subscription fee
    public static final BigDecimal YEARLY_SUBSCRIPTION_FEE = new BigDecimal("150.00");

    // Commission rate for commission-based subscriptions (10%)
    public static final BigDecimal COMMISSION_BASED_RATE = new BigDecimal("10.00");

    // Zero commission rate for monthly and yearly subscriptions
    public static final BigDecimal ZERO_COMMISSION_RATE = BigDecimal.ZERO;
}