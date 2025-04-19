package com.incon.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    // Getters and Setters
    private int productId;
    private String name;
    private String description;
    private float price;
    private int stockQuantity;
    private String category;
    private String image;
    private float rating;
}
