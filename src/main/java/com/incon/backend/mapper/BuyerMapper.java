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
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "userEmail", source = "userEmail")
    @Mapping(target = "userFullName", source = "userFullName")
    @Mapping(target = "userPosition", source = "userPosition")
    @Mapping(target = "userBusinessPhone", source = "userBusinessPhone")
    @Mapping(target = "userIsVerified", source = "userIsVerified")
    Buyer toBuyer(BuyerRequest request);

    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "userEmail", source = "userEmail")
    @Mapping(target = "userFullName", source = "userFullName")
    @Mapping(target = "userPosition", source = "userPosition")
    @Mapping(target = "userBusinessPhone", source = "userBusinessPhone")
    @Mapping(target = "userIsVerified", source = "userIsVerified")
    @Mapping(target = "cartId", source = "buyerCart.cartId")
    @Mapping(target = "orderIds", expression = "java(mapOrders(buyer.getBuyerOrders()))")
    BuyerResponse toBuyerResponse(Buyer buyer);

    default List<Integer> mapOrders(List<Order> orders) {
        if (orders == null) return null;
        return orders.stream()
                .map(Order::getOrderId)
                .collect(Collectors.toList());
    }
}