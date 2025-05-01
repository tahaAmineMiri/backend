package com.incon.backend.service;

import com.incon.backend.dto.request.CompanyRequest;
import com.incon.backend.dto.response.CompanyResponse;
import com.incon.backend.entity.Company;
import com.incon.backend.enums.VerificationStatus;

import java.util.List;

public interface CompanyService {
    /**
     * Create a new company associated with a user
     *
     * @param userId The user ID to associate with the company
     * @param companyRequest The company details
     * @return The company response
     */
    CompanyResponse createCompany(Integer userId, CompanyRequest companyRequest);

    /**
     * Get a company by its ID
     *
     * @param companyId The company ID
     * @return The company response
     */
    CompanyResponse getCompanyById(Integer companyId);

    /**
     * Get a company by its associated user ID
     *
     * @param userId The user ID
     * @return The company response
     */
    CompanyResponse getCompanyByUserId(Integer userId);

    /**
     * Get all companies
     *
     * @return List of company responses
     */
    List<CompanyResponse> getAllCompanies();

    /**
     * Get all companies with a specific verification status
     *
     * @param status The verification status
     * @return List of company responses
     */
    List<CompanyResponse> getCompaniesByVerificationStatus(VerificationStatus status);

    /**
     * Get all companies in a specific industry category
     *
     * @param category The industry category
     * @return List of company responses
     */
    List<CompanyResponse> getCompaniesByIndustryCategory(String category);

    /**
     * Update company information
     *
     * @param companyId The company ID
     * @param companyRequest The updated company details
     * @return The updated company response
     */
    CompanyResponse updateCompany(Integer companyId, CompanyRequest companyRequest);

    /**
     * Update company verification status
     *
     * @param companyId The company ID
     * @param status The new verification status
     * @return The updated company response
     */
    CompanyResponse updateCompanyVerificationStatus(Integer companyId, VerificationStatus status);

    /**
     * Delete a company by its ID
     *
     * @param companyId The company ID
     */
    void deleteCompany(Integer companyId);

    /**
     * Get company entity by ID (for internal use)
     *
     * @param companyId The company ID
     * @return The company entity
     */
    Company getCompanyEntityById(Integer companyId);
}