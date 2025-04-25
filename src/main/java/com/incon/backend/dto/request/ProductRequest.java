package com.incon.backend.dto.request;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Setter
@Getter
public class ProductRequest {

    // Getters and Setters
    @NotBlank(message = "Product name is required")
    @Size(min = 3, max = 100, message = "Product name must be between 3 and 100 characters")
    private String name;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price cannot be negative")
    private BigDecimal price;

    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity cannot be negative")
    private Integer stockQuantity;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Image URL is required")
    private String image;

    // Constructors
    public ProductRequest() {
    }

    public ProductRequest(String name, String description, BigDecimal price, Integer stockQuantity,
                          String category, String image) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
        this.image = image;
    }
}