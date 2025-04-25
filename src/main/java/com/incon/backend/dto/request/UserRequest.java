package com.incon.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import com.incon.backend.enums.Role;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email")
    private String userEmail;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password should be at least 8 characters long")
    private String userPassword;

    @NotBlank(message = "Full name is required")
    private String userFullName;

    @NotBlank(message = "Position is required")
    private String userPosition;

    @NotBlank(message = "Business phone is required")
    private String userBusinessPhone;

    @NotNull(message = "Role is required")
    private Role userRole;
}