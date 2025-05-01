package com.incon.backend.dto.response;

import com.incon.backend.enums.VerificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyResponse {
    private Integer companyId;
    private String companyName;
    private String companyAddress;
    private String companyIndustryCategory;
    private String companyCommercialRegister;
    private String companyBusinessLicenseNumber;
    private String companyFiscalIdentifier;
    private String companySocialSecurityNumber;
    private String companyCommonIdentifier;
    private boolean companyIsVerified;
    private VerificationStatus companyVerificationStatus;
    private Integer userId;
    private LocalDateTime companyCreatedAt;
}