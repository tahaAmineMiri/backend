//package com.incon.backend.service.impl;
//
//import com.incon.backend.dto.request.PaymentRequest;
//import com.incon.backend.dto.response.PaymentResponse;
//import com.incon.backend.entity.Order;
//import com.incon.backend.entity.Payment;
//import com.incon.backend.enums.OrderStatus;
//import com.incon.backend.enums.PaymentStatus;
//import com.incon.backend.exception.BadRequestException;
//import com.incon.backend.exception.ResourceNotFoundException;
//import com.incon.backend.mapper.PaymentMapper;
//import com.incon.backend.repository.OrderRepository;
//import com.incon.backend.repository.PaymentRepository;
//import com.incon.backend.service.PaymentService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class PaymentServiceImpl implements PaymentService {
//
//    private final PaymentRepository paymentRepository;
//    private final OrderRepository orderRepository;
//    private final PaymentMapper paymentMapper;
//
//    @Override
//    @Transactional(readOnly = true)
//    public PaymentResponse getPaymentById(Integer paymentId) {
//        Payment payment = paymentRepository.findById(paymentId)
//                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));
//
//        return paymentMapper.toResponse(payment);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public PaymentResponse getPaymentByOrderId(Integer orderId) {
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
//
//        Payment payment = paymentRepository.findByPaymentOrder(order)
//                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for order with id: " + orderId));
//
//        return paymentMapper.toResponse(payment);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<PaymentResponse> getPaymentsByStatus(PaymentStatus paymentStatus) {
//        List<Payment> payments = paymentRepository.findByPaymentStatus(paymentStatus);
//
//        return payments.stream()
//                .map(paymentMapper::toResponse)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    @Transactional
//    public PaymentResponse updatePaymentReference(Integer paymentId, String paymentReferenceNumber) {
//        Payment payment = paymentRepository.findById(paymentId)
//                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));
//
//        payment.setPaymentReferenceNumber(paymentReferenceNumber);
//        Payment updatedPayment = paymentRepository.save(payment);
//
//        return paymentMapper.toResponse(updatedPayment);
//    }
//
//    @Override
//    @Transactional
//    public PaymentResponse verifyPayment(Integer paymentId) {
//        Payment payment = paymentRepository.findById(paymentId)
//                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));
//
//        if (payment.getPaymentStatus() == PaymentStatus.VERIFIED) {
//            throw new BadRequestException("Payment is already verified");
//        }
//
//        payment.setPaymentStatus(PaymentStatus.VERIFIED);
//        Payment verifiedPayment = paymentRepository.save(payment);
//
//        // Update order status to PROCESSING once payment is verified
//        Order order = payment.getPaymentOrder();
//        if (order != null && order.getOrderStatus() == OrderStatus.PENDING) {
//            order.setOrderStatus(OrderStatus.PROCESSING);
//            orderRepository.save(order);
//        }
//
//        return paymentMapper.toResponse(verifiedPayment);
//    }
//
//    @Override
//    @Transactional
//    public PaymentResponse rejectPayment(Integer paymentId) {
//        Payment payment = paymentRepository.findById(paymentId)
//                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));
//
//        if (payment.getPaymentStatus() == PaymentStatus.REJECTED) {
//            throw new BadRequestException("Payment is already rejected");
//        }
//
//        payment.setPaymentStatus(PaymentStatus.REJECTED);
//        Payment rejectedPayment = paymentRepository.save(payment);
//
//        // Cancel order if payment is rejected
//        Order order = payment.getPaymentOrder();
//        if (order != null && order.getOrderStatus() != OrderStatus.CANCELLED) {
//            order.setOrderStatus(OrderStatus.CANCELLED);
//            orderRepository.save(order);
//        }
//
//        return paymentMapper.toResponse(rejectedPayment);
//    }
//}