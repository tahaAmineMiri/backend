package com.incon.backend.entity;

import com.incon.backend.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "users") // 'user' is a reserved keyword in some databases
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Column(nullable = false)
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @NotBlank(message = "Password is required")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Role is required")
    private Role role;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Position is required")
    private String position;

    @NotBlank(message = "Business phone is required")
    private String businessPhone;

    @NotNull
    private boolean isVerified = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}