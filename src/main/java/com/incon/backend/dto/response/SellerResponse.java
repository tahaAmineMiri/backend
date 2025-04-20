package com.incon.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
public class SellerResponse extends UserResponse {
    public SellerResponse(int id, String email, String fullName, String position, String businessPhone, boolean isVerified) {}
}