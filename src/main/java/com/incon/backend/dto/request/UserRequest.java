package com.incon.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.incon.backend.enums.Role; // Assuming your Role enum is in this package

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password should be at least 8 characters long")
    private String password;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Position is required")
    private String position;

    @NotBlank(message = "Business phone is required")
    private String businessPhone; // This might be optional for Buyer

    @NotNull(message = "Role is required")
    private Role role; // This can be a Seller, Buyer, or Admin
}
