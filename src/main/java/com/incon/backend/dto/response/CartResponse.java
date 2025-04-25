package com.incon.backend.dto.response;

import com.incon.backend.entity.Buyer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private Integer cartId;
    private BuyerResponse buyerResponse;
    private List<CartItemResponse> items = new ArrayList<>();
    private BigDecimal totalAmount;
}