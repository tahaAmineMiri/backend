package com.incon.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private Integer cartId;
    private Float totalAmount;
    private Date createdAt;
    private Date updatedAt;
    private Integer buyerId;
    private List<CartItemResponse> items;
}