package com.incon.backend.dto.response;

import lombok.*;

@Getter
@Setter
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
