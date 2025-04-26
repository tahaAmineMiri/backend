package com.incon.backend.dto.response;

import com.incon.backend.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Integer orderId;
    private Integer buyerId;
    private OrderStatus orderStatus;
    private Date orderDate;
    private BigDecimal orderTotalAmount;
    private String orderShippingAddress;
    private PaymentResponse paymentResponse;
    private List<OrderItemResponse> orderItems;
}