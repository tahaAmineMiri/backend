package com.incon.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SellerResponse extends UserResponse {
    // No additional fields needed for the basic implementation

    public SellerResponse(
            int id, String email, String fullName, String position, String businessPhone, boolean isVerified
    ) {
        super(id, email, fullName, position, businessPhone, isVerified);
    }
}