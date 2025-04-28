// AdminRequest.java
package com.incon.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminRequest {

    @NotBlank(message = "Admin name is required")
    private String adminName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email")
    private String adminEmail;

    @NotBlank(message = "Password is required")
    private String adminPassword;
}