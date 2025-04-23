package com.incon.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
public class BuyerRequest extends UserRequest {
    // Add buyer-specific fields if needed
}