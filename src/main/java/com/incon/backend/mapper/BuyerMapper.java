package com.incon.backend.mapper;

import com.incon.backend.dto.request.BuyerRequest;
import com.incon.backend.dto.response.BuyerResponse;
import com.incon.backend.entity.Buyer;
import com.incon.backend.entity.Order;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BuyerMapper {
    @Mapping(target = "userPassword", source = "userPassword")
    @Mapping(target = "userEmail", source = "userEmail")
    @Mapping(target = "userFullName", source = "userFullName")
    @Mapping(target = "userPosition", source = "userPosition")
    @Mapping(target = "userBusinessPhone", source = "userBusinessPhone")
    @Mapping(target = "userRole", source = "userRole")
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "userIsVerified", constant = "false")
    @Mapping(target = "userCreatedAt", ignore = true)
    @Mapping(target = "userUpdatedAt", ignore = true)
    @Mapping(target = "buyerCart", ignore = true)
    @Mapping(target = "buyerOrders", ignore = true)
    Buyer toBuyer(BuyerRequest request);

    // Create a custom method for mapping
    default BuyerResponse toBuyerResponse(Buyer buyer) {
        if (buyer == null) {
            return null;
        }

        // Manual builder approach
        BuyerResponse.BuyerResponseBuilder<?, ?> builder = BuyerResponse.builder()
                .userId(buyer.getUserId())
                .userEmail(buyer.getUserEmail())
                .userFullName(buyer.getUserFullName())
                .userPosition(buyer.getUserPosition())
                .userBusinessPhone(buyer.getUserBusinessPhone())
                .userIsVerified(buyer.isUserIsVerified())
                .cartId(buyer.getBuyerCart() != null ? buyer.getBuyerCart().getCartId() : null);

        // Map the orders
        List<Integer> orderIds = null;
        if (buyer.getBuyerOrders() != null) {
            orderIds = buyer.getBuyerOrders().stream()
                    .map(Order::getOrderId)
                    .collect(Collectors.toList());
        }
        builder.orderIds(orderIds);

        return builder.build();
    }
}