package com.incon.backend.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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

    // Constructors
    public ProductResponse() {
    }

}
