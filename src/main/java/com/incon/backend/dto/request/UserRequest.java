//package com.incon.backend.dto.request;
//
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotBlank;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class UserRequest {
//
//    @NotBlank(message = "Email is required")
//    @Email(message = "Please provide a valid email")
//    private String email;
//
//    @NotBlank(message = "Password is required")
//    private String password;
//
//    @NotBlank(message = "Full name is required")
//    private String fullName;
//
//    @NotBlank(message = "Position is required")
//    private String position;
//
//    @NotBlank(message = "Business Phone is required")
//    private String businessPhone;
//}