package com.incon.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemRequest {
    private Integer cartId;        // Represents cart.cartId
    private Integer productId;     // Represents product.productId
    private Integer quantity;
    private BigDecimal itemPrice;  // Renamed from price to match entity
}