package com.incon.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {
    private Integer orderItemId;
    private Integer orderItemQuantity;
    private BigDecimal orderItemPrice;
    private BigDecimal orderItemSubtotal;
    private Integer productId;
    private String productName;
    private String productImage;
}