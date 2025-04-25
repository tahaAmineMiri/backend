package com.incon.backend.dto.request;

import java.math.BigDecimal;

public class OrderItemRequest {
    // This is the request class for order items
    private Integer orderItemId;
    private Integer quantity;
    private Integer productId;
    private String productName;
    private String productImage;
    private String productDescription;
    private String productCategory;
    private BigDecimal productPrice;
}
