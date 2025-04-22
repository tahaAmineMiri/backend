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

    Buyer toEntity(BuyerRequest request);

    @Mapping(target = "cartId", source = "cart.cartId")
    @Mapping(target = "orderIds", expression = "java(mapOrders(buyer.getOrders()))")
    BuyerResponse toResponse(Buyer buyer);

    default List<Long> mapOrders(List<Order> orders) {
        if (orders == null) return null;
        return orders.stream()
                .map(Order::getOrderId)
                .map(Integer::longValue)  // Convert Integer to Long
                .collect(Collectors.toList());
    }
}
