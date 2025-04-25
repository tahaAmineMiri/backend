package com.incon.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {
    private Integer cartItemId;
    private Integer quantity;
    private BigDecimal itemPrice;
    private BigDecimal subtotal;
    private Integer productId;
    private String productName;
    private String productImage;
}