package com.incon.backend.service.impl;

import com.incon.backend.dto.request.CompanyRequest;
import com.incon.backend.dto.response.CompanyResponse;
import com.incon.backend.entity.Company;
import com.incon.backend.entity.User;
import com.incon.backend.enums.VerificationStatus;
import com.incon.backend.exception.BadRequestException;
import com.incon.backend.exception.ResourceNotFoundException;
import com.incon.backend.mapper.CompanyMapper;
import com.incon.backend.repository.CompanyRepository;
import com.incon.backend.repository.UserRepository;
import com.incon.backend.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final CompanyMapper companyMapper;

    @Override
    @Transactional
    public CompanyResponse createCompany(Integer userId, CompanyRequest companyRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Check if user already has a company
        if (companyRepository.findByCompanyUser(user).isPresent()) {
            throw new BadRequestException("User already has a company registered");
        }

        // Validate unique identifiers if provided
        validateUniqueIdentifiers(companyRequest);

        // Create company
        Company company = companyMapper.toCompany(companyRequest);
        company.setCompanyUser(user);
        company.setCompanyVerificationStatus(VerificationStatus.PENDING);

        Company savedCompany = companyRepository.save(company);

        // Update user company reference
        user.setUserCompany(savedCompany);
        userRepository.save(user);

        return companyMapper.toCompanyResponse(savedCompany);
    }

    @Override
    @Transactional(readOnly = true)
    public CompanyResponse getCompanyById(Integer companyId) {
        Company company = getCompanyEntityById(companyId);
        return companyMapper.toCompanyResponse(company);
    }

    // In CompanyServiceImpl.java, find this method:
    @Override
    @Transactional(readOnly = true)
    public CompanyResponse getCompanyByUserId(Integer userId) {
        // Update this line to use the correct method name
        Company company = companyRepository.findByCompanyUser_UserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found for user with id: " + userId));
        return companyMapper.toCompanyResponse(company);
    }


    @Override
    @Transactional(readOnly = true)
    public List<CompanyResponse> getAllCompanies() {
        return companyRepository.findAll().stream()
                .map(companyMapper::toCompanyResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyResponse> getCompaniesByVerificationStatus(VerificationStatus status) {
        return companyRepository.findByCompanyVerificationStatus(status).stream()
                .map(companyMapper::toCompanyResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyResponse> getCompaniesByIndustryCategory(String category) {
        return companyRepository.findByCompanyIndustryCategory(category).stream()
                .map(companyMapper::toCompanyResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CompanyResponse updateCompany(Integer companyId, CompanyRequest companyRequest) {
        Company company = getCompanyEntityById(companyId);

        // Validate unique identifiers for update
        validateUniqueIdentifiersForUpdate(companyId, companyRequest);

        // Update company fields
        companyMapper.updateCompanyFromRequest(companyRequest, company);

        // Save updated company
        Company updatedCompany = companyRepository.save(company);

        return companyMapper.toCompanyResponse(updatedCompany);
    }

    @Override
    @Transactional
    public CompanyResponse updateCompanyVerificationStatus(Integer companyId, VerificationStatus status) {
        Company company = getCompanyEntityById(companyId);

        company.setCompanyVerificationStatus(status);
        if (status == VerificationStatus.VERIFIED) {
            company.setCompanyIsVerified(true);

            // Also update user's verification status
            if (company.getCompanyUser() != null) {
                User user = company.getCompanyUser();
                user.setUserIsVerified(true);
                userRepository.save(user);
            }
        } else if (status == VerificationStatus.REJECTED) {
            company.setCompanyIsVerified(false);

            // Also update user's verification status
            if (company.getCompanyUser() != null) {
                User user = company.getCompanyUser();
                user.setUserIsVerified(false);
                userRepository.save(user);
            }
        }

        Company updatedCompany = companyRepository.save(company);
        return companyMapper.toCompanyResponse(updatedCompany);
    }

    @Override
    @Transactional
    public void deleteCompany(Integer companyId) {
        Company company = getCompanyEntityById(companyId);

        // Remove reference from user if exists
        if (company.getCompanyUser() != null) {
            User user = company.getCompanyUser();
            user.setUserCompany(null);
            userRepository.save(user);
        }

        companyRepository.delete(company);
    }

    @Override
    @Transactional(readOnly = true)
    public Company getCompanyEntityById(Integer companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + companyId));
    }

    /**
     * Validates that unique identifiers are not already in use
     */
    private void validateUniqueIdentifiers(CompanyRequest request) {
        if (request.getCompanyCommercialRegister() != null &&
                !request.getCompanyCommercialRegister().isEmpty() &&
                companyRepository.existsByCompanyCommercialRegister(request.getCompanyCommercialRegister())) {
            throw new BadRequestException("Commercial register number is already registered");
        }

        if (request.getCompanyBusinessLicenseNumber() != null &&
                !request.getCompanyBusinessLicenseNumber().isEmpty() &&
                companyRepository.existsByCompanyBusinessLicenseNumber(request.getCompanyBusinessLicenseNumber())) {
            throw new BadRequestException("Business license number is already registered");
        }

        if (request.getCompanyFiscalIdentifier() != null &&
                !request.getCompanyFiscalIdentifier().isEmpty() &&
                companyRepository.existsByCompanyFiscalIdentifier(request.getCompanyFiscalIdentifier())) {
            throw new BadRequestException("Fiscal identifier is already registered");
        }

        if (request.getCompanySocialSecurityNumber() != null &&
                !request.getCompanySocialSecurityNumber().isEmpty() &&
                companyRepository.existsByCompanySocialSecurityNumber(request.getCompanySocialSecurityNumber())) {
            throw new BadRequestException("Social security number is already registered");
        }

        if (request.getCompanyCommonIdentifier() != null &&
                !request.getCompanyCommonIdentifier().isEmpty() &&
                companyRepository.existsByCompanyCommonIdentifier(request.getCompanyCommonIdentifier())) {
            throw new BadRequestException("Common identifier is already registered");
        }
    }

    /**
     * Validates unique identifiers for update operations
     */
    private void validateUniqueIdentifiersForUpdate(Integer companyId, CompanyRequest request) {
        Company existingCompany = getCompanyEntityById(companyId);

        if (request.getCompanyCommercialRegister() != null &&
                !request.getCompanyCommercialRegister().isEmpty() &&
                !request.getCompanyCommercialRegister().equals(existingCompany.getCompanyCommercialRegister()) &&
                companyRepository.existsByCompanyCommercialRegister(request.getCompanyCommercialRegister())) {
            throw new BadRequestException("Commercial register number is already registered");
        }

        if (request.getCompanyBusinessLicenseNumber() != null &&
                !request.getCompanyBusinessLicenseNumber().isEmpty() &&
                !request.getCompanyBusinessLicenseNumber().equals(existingCompany.getCompanyBusinessLicenseNumber()) &&
                companyRepository.existsByCompanyBusinessLicenseNumber(request.getCompanyBusinessLicenseNumber())) {
            throw new BadRequestException("Business license number is already registered");
        }

        if (request.getCompanyFiscalIdentifier() != null &&
                !request.getCompanyFiscalIdentifier().isEmpty() &&
                !request.getCompanyFiscalIdentifier().equals(existingCompany.getCompanyFiscalIdentifier()) &&
                companyRepository.existsByCompanyFiscalIdentifier(request.getCompanyFiscalIdentifier())) {
            throw new BadRequestException("Fiscal identifier is already registered");
        }

        if (request.getCompanySocialSecurityNumber() != null &&
                !request.getCompanySocialSecurityNumber().isEmpty() &&
                !request.getCompanySocialSecurityNumber().equals(existingCompany.getCompanySocialSecurityNumber()) &&
                companyRepository.existsByCompanySocialSecurityNumber(request.getCompanySocialSecurityNumber())) {
            throw new BadRequestException("Social security number is already registered");
        }

        if (request.getCompanyCommonIdentifier() != null &&
                !request.getCompanyCommonIdentifier().isEmpty() &&
                !request.getCompanyCommonIdentifier().equals(existingCompany.getCompanyCommonIdentifier()) &&
                companyRepository.existsByCompanyCommonIdentifier(request.getCompanyCommonIdentifier())) {
            throw new BadRequestException("Common identifier is already registered");
        }
    }
}