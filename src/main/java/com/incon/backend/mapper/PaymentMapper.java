package com.incon.backend.mapper;

import com.incon.backend.dto.response.PaymentResponse;
import com.incon.backend.entity.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentResponse toResponse(Payment payment);
}