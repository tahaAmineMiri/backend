package com.incon.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRequest {

    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "Address is required")
    private String companyAddress;

    @NotBlank(message = "Industry category is required")
    private String companyIndustryCategory;

    private String companyCommercialRegister;

    private String companyBusinessLicenseNumber;

    private String companyFiscalIdentifier;

    private String companySocialSecurityNumber;

    private String companyCommonIdentifier;
}