package com.incon.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private Integer cartId;
    private BigDecimal totalAmount;
    private Date createdAt;
    private Date updatedAt;
    private Integer buyerId;
    private List<CartItemResponse> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartItemResponse {
        private Integer cartItemId;
        private ProductResponse product;
        private Integer quantity;
        private BigDecimal itemPrice;
        private BigDecimal subtotal;
    }
}