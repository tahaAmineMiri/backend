package com.incon.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequest {
    private Integer orderItemId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer orderItemQuantity;

    @NotNull(message = "Product ID is required")
    private Integer productId;

    private String productName;
    private String productImage;
    private String productDescription;
    private String productCategory;
    private BigDecimal productPrice;
}