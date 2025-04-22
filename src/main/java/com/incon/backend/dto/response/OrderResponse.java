package com.incon.backend.dto.response;

import com.incon.backend.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Integer orderId;
    private OrderStatus status;
    private Date orderDate;
    private Float totalAmount;
    private String shippingDetails;
    private Integer buyerId;
    private List<OrderItemResponse> items;
    private PaymentResponse payment;
}