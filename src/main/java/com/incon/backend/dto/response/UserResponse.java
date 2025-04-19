package com.incon.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private int id;
    private String email;
    private String fullName;
    private String position;
    private String businessPhone;
    private boolean isVerified;
}
