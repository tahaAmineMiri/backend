package com.incon.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "admins")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer adminId;

    @Column(nullable = false)
    @NotBlank(message = "Admin name is required")
    private String adminName;

    @Column(unique = true, nullable = false)
    @Email(message = "Invalid email format")
    @NotBlank(message = "Admin email is required")
    private String adminEmail;

    @Column(nullable = false)
    @NotBlank(message = "Password is required")
    private String adminPassword;

    @Column(nullable = false)
    private String adminRole = "ADMIN";

    @CreationTimestamp
    private LocalDateTime adminCreatedAt;

    @UpdateTimestamp
    private LocalDateTime adminUpdatedAt;
}