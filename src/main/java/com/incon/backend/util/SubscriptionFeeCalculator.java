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

    /**
     * Calculate the subscription amount based on subscription type
     *
     * @param subscriptionType The type of subscription
     * @return The subscription amount
     */
    public static BigDecimal calculateSubscriptionAmount(SubscriptionType subscriptionType) {
        switch (subscriptionType) {
            case MONTHLY:
                return SubscriptionConstants.MONTHLY_SUBSCRIPTION_FEE;
            case YEARLY:
                return SubscriptionConstants.YEARLY_SUBSCRIPTION_FEE;
            case COMMISSION_BASED:
                // No upfront fee for commission-based subscriptions
                return BigDecimal.ZERO;
            default:
                return SubscriptionConstants.MONTHLY_SUBSCRIPTION_FEE;
        }
    }

    /**
     * Calculate the commission rate based on subscription type
     *
     * @param subscriptionType The type of subscription
     * @return The commission rate
     */
    public static BigDecimal calculateCommissionRate(SubscriptionType subscriptionType) {
        switch (subscriptionType) {
            case MONTHLY:
            case YEARLY:
                // No commission for monthly and yearly subscriptions
                return SubscriptionConstants.ZERO_COMMISSION_RATE;
            case COMMISSION_BASED:
                // 10% commission for commission-based subscriptions
                return SubscriptionConstants.COMMISSION_BASED_RATE;
            default:
                return SubscriptionConstants.ZERO_COMMISSION_RATE;
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
        // If no subscription or not commission-based, no commission fee
        if (subscription == null || subscription.getSubscriptionType() != SubscriptionType.COMMISSION_BASED) {
            return BigDecimal.ZERO;
        }

        // Calculate commission fee (price * commission rate / 100)
        return product.getProductPrice()
                .multiply(SubscriptionConstants.COMMISSION_BASED_RATE)
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
}