package com.incon.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    @NotNull(message = "Buyer ID is required")
    private Integer buyerId;

    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;  // Renamed from shippingDetails to match entity

    private String billingAddress;   // Added to match entity

    private String notes;            // Additional field for order notes
}