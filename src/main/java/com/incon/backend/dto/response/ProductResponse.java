package com.incon.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private int productId;
    private String productName;
    private String productDescription;
    private BigDecimal productPrice;
    private int productStockQuantity;
    private String productCategory;
    private String productImage;
    private float productRating;
}