package com.incon.backend.dto.response;

import com.incon.backend.enums.PaymentMethod;
import com.incon.backend.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private Integer paymentId;
    private BigDecimal paymentAmount;
    private Date paymentDate;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private String paymentReferenceNumber;
    private Integer orderId;
}