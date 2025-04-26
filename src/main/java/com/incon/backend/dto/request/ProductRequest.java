package com.incon.backend.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@NoArgsConstructor
@Setter
@Getter
public class ProductRequest {

    @NotBlank(message = "Product name is required")
    @Size(min = 3, max = 100, message = "Product name must be between 3 and 100 characters")
    private String productName;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String productDescription;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price cannot be negative")
    private BigDecimal productPrice;

    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity cannot be negative")
    private Integer productStockQuantity;

    @NotBlank(message = "Category is required")
    private String productCategory;

    @NotBlank(message = "Image URL is required")
    private String productImage;

    // Constructors

    public ProductRequest(String productName, String productDescription, BigDecimal productPrice,
                          Integer productStockQuantity, String productCategory, String productImage) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productStockQuantity = productStockQuantity;
        this.productCategory = productCategory;
        this.productImage = productImage;
    }
}