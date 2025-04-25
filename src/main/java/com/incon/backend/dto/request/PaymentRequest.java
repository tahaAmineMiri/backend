package com.incon.backend.dto.request;

import com.incon.backend.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    @NotNull(message = "Order ID is required")
    private Integer orderId;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    private LocalDateTime paymentDate;

    private String paymentReferenceNumber;
}