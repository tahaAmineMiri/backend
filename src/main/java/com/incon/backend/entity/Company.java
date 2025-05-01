package com.incon.backend.entity;

import com.incon.backend.enums.VerificationStatus;
import jakarta.persistence.*;
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
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer companyId;

    @Column(nullable = false)
    @NotBlank(message = "Company name is required")
    private String companyName;

    @Column(nullable = false)
    @NotBlank(message = "Address is required")
    private String companyAddress;

    @Column(nullable = false)
    @NotBlank(message = "Industry category is required")
    private String companyIndustryCategory;

    @Column(unique = true)
    private String companyCommercialRegister;

    @Column(unique = true)
    private String companyBusinessLicenseNumber;

    @Column(unique = true)
    private String companyFiscalIdentifier;

    @Column(unique = true)
    private String companySocialSecurityNumber;

    @Column(unique = true)
    private String companyCommonIdentifier;

    @Builder.Default
    private boolean companyIsVerified = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private VerificationStatus companyVerificationStatus = VerificationStatus.PENDING;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User companyUser;

    @CreationTimestamp
    private LocalDateTime companyCreatedAt;

    @UpdateTimestamp
    private LocalDateTime companyUpdatedAt;
}