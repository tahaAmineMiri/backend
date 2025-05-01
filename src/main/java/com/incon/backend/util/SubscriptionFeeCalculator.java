package com.incon.backend.util;

import com.incon.backend.entity.Product;
import com.incon.backend.entity.Subscription;
import com.incon.backend.enums.SubscriptionType;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Utility class for calculating subscription fees and commissions
 */
public class SubscriptionFeeCalculator {

    // Base monthly subscription fee
    private static final BigDecimal BASE_MONTHLY_FEE = new BigDecimal("49.99");

    // Base yearly subscription fee (10% discount compared to paying monthly for a year)
    private static final BigDecimal BASE_YEARLY_FEE = new BigDecimal("539.89");

    // Default commission rate for COMMISSION_BASED subscriptions
    private static final BigDecimal DEFAULT_COMMISSION_RATE = new BigDecimal("5.00");

    /**
     * Calculate the subscription amount based on subscription type
     *
     * @param subscriptionType The type of subscription
     * @return The subscription amount
     */
    public static BigDecimal calculateSubscriptionAmount(SubscriptionType subscriptionType) {
        switch (subscriptionType) {
            case MONTHLY:
                return BASE_MONTHLY_FEE;
            case YEARLY:
                return BASE_YEARLY_FEE;
            case COMMISSION_BASED:
                return BigDecimal.ZERO; // No upfront fee for commission-based subscriptions
            default:
                return BASE_MONTHLY_FEE;
        }
    }

    /**
     * Calculate commission fee for a product based on its price and the subscription
     *
     * @param product The product
     * @param subscription The subscription
     * @return The commission fee amount
     */
    public static BigDecimal calculateCommissionFee(Product product, Subscription subscription) {
        // If not a commission-based subscription, no commission fee
        if (subscription == null || subscription.getSubscriptionType() != SubscriptionType.COMMISSION_BASED) {
            return BigDecimal.ZERO;
        }

        BigDecimal commissionRate = subscription.getSubscriptionCommissionRate();
        if (commissionRate == null || commissionRate.compareTo(BigDecimal.ZERO) == 0) {
            commissionRate = DEFAULT_COMMISSION_RATE;
        }

        // Calculate commission fee (price * commission rate / 100)
        return product.getProductPrice()
                .multiply(commissionRate)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }

    /**
     * Calculate the total price with commission for a product
     *
     * @param product The product
     * @param subscription The subscription
     * @return The total price including commission
     */
    public static BigDecimal calculateTotalPriceWithCommission(Product product, Subscription subscription) {
        BigDecimal commissionFee = calculateCommissionFee(product, subscription);
        return product.getProductPrice().add(commissionFee);
    }

    /**
     * Get the default commission rate for a new subscription
     *
     * @return The default commission rate
     */
    public static BigDecimal getDefaultCommissionRate() {
        return DEFAULT_COMMISSION_RATE;
    }
}