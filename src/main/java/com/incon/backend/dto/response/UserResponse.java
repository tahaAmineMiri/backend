package com.incon.backend.dto.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserResponse {
    private Integer userId;
    private String userEmail;
    private String userFullName;
    private String userPosition;
    private String userBusinessPhone;
    private boolean userIsVerified;
}