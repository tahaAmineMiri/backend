package com.incon.backend.dto.response;

import com.incon.backend.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Role role;
    private int id;
    private String username;
    private String email;
    private String fullName;
    private String position;
    private String businessPhone;
    private boolean isVerified;
    private String message;

    public UserResponse() {}

    public UserResponse(Role role, String username, String email, String fullName, String message) {
        this.role = role;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.message = message;
    }
}
