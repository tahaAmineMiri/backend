package com.incon.backend.dto.request;

import java.math.BigDecimal;

public class CartItemRequest {
    private Integer cartId;
    private Integer productId;
    private Integer quantity;
    private BigDecimal price;
}
