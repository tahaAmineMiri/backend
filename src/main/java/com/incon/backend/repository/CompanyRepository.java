package com.incon.backend.repository;

import com.incon.backend.entity.Company;
import com.incon.backend.entity.User;
import com.incon.backend.enums.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
    Optional<Company> findByCompanyUser(User user);

    // Change this method name to match the actual field name in User entity
    Optional<Company> findByCompanyUser_UserId(Integer userId);

    List<Company> findByCompanyVerificationStatus(VerificationStatus verificationStatus);
    List<Company> findByCompanyIndustryCategory(String industryCategory);
    boolean existsByCompanyCommercialRegister(String commercialRegister);
    boolean existsByCompanyBusinessLicenseNumber(String businessLicenseNumber);
    boolean existsByCompanyFiscalIdentifier(String fiscalIdentifier);
    boolean existsByCompanySocialSecurityNumber(String socialSecurityNumber);
    boolean existsByCompanyCommonIdentifier(String commonIdentifier);
}