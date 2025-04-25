package com.incon.backend.mapper;

import com.incon.backend.dto.response.PaymentResponse;
import com.incon.backend.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    @Mapping(source = "paymentId", target = "paymentId")
    @Mapping(source = "paymentAmount", target = "paymentAmount")
    @Mapping(source = "paymentDate", target = "paymentDate")
    @Mapping(source = "paymentMethod", target = "paymentMethod")
    @Mapping(source = "paymentStatus", target = "paymentStatus")
    @Mapping(source = "paymentReferenceNumber", target = "paymentReferenceNumber")
    @Mapping(source = "paymentOrder.orderId", target = "orderId")
    PaymentResponse toResponse(Payment payment);
}