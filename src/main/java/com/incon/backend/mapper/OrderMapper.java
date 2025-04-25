package com.incon.backend.mapper;

import com.incon.backend.dto.response.OrderResponse;
import com.incon.backend.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class, PaymentMapper.class})
public interface OrderMapper {
    @Mapping(source = "orderId", target = "orderId")
    @Mapping(source = "orderStatus", target = "orderStatus")
    @Mapping(source = "orderDate", target = "orderDate")
    @Mapping(source = "orderTotalAmount", target = "orderTotalAmount")
    @Mapping(source = "orderShippingAddress", target = "orderShippingAddress")
    @Mapping(source = "orderBuyer.userId", target = "buyerId")
    @Mapping(source = "orderItems", target = "orderItems")
    @Mapping(source = "orderPayment", target = "paymentResponse")
    OrderResponse toResponse(Order order);
}