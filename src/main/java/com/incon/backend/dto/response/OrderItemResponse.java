package com.incon.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {
    private Integer orderItemId;
    private Integer quantity;
    private Float itemPrice;
    private Float subtotal;
    private Integer productId;
    private String productName;
    private String productImage;
}