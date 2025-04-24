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
    private OrderStatus status;
    private Date orderDate;
    private BigDecimal totalAmount;
    private String shippingDetails;
    private PaymentResponse payment;
    private List<OrderItemResponse> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemResponse {
        private Integer orderItemId;
        private ProductResponse product;
        private Integer quantity;
        private BigDecimal itemPrice;
        private BigDecimal subtotal;
    }
}