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
    private String name;
    private String description;
    private BigDecimal price;  // Changed from float to BigDecimal to match entity
    private int stockQuantity;
    private String category;
    private String image;
    private float rating;
}