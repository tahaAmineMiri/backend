package com.incon.backend.dto.response;

import com.incon.backend.enums.PaymentMethod;
import com.incon.backend.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private Integer paymentId;
    private Float amount;
    private Date paymentDate;
    private PaymentMethod method;
    private PaymentStatus status;
    private String referenceNumber;
    private Integer orderId;
}