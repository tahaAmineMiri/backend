package com.incon.backend.service;

import com.incon.backend.dto.request.PaymentRequest;
import com.incon.backend.dto.response.PaymentResponse;
import com.incon.backend.enums.PaymentStatus;

import java.util.List;

public interface PaymentService {
    PaymentResponse getPaymentById(Integer paymentId);
    PaymentResponse getPaymentByOrderId(Integer orderId);
    List<PaymentResponse> getPaymentsByStatus(PaymentStatus status);
    PaymentResponse updatePaymentReference(Integer paymentId, String referenceNumber);
    PaymentResponse verifyPayment(Integer paymentId);
    PaymentResponse rejectPayment(Integer paymentId);
}