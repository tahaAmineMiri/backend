package com.incon.backend.mapper;

import com.incon.backend.dto.response.OrderResponse;
import com.incon.backend.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class, PaymentMapper.class})
public interface OrderMapper {

    @Mappings({
            @Mapping(source = "orderId", target = "orderId"),
            @Mapping(source = "status", target = "status"),
            @Mapping(source = "orderDate", target = "orderDate"),
            @Mapping(source = "totalAmount", target = "totalAmount"),
            @Mapping(source = "shippingDetails", target = "shippingDetails"),
            @Mapping(source = "buyer.id", target = "buyerId"),
            @Mapping(source = "orderItems", target = "items"),
            @Mapping(source = "payment", target = "payment")
    })
    OrderResponse toResponse(Order order);
}
