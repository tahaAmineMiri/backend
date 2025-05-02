package com.incon.backend.controller;

import com.incon.backend.dto.request.CompanyRequest;
import com.incon.backend.dto.response.CompanyResponse;
import com.incon.backend.entity.User;
import com.incon.backend.enums.VerificationStatus;
import com.incon.backend.integration.BaseIntegrationTest;
import com.incon.backend.repository.CompanyRepository;
import com.incon.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

public class CompanyControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreateCompany() {
        // Get a user
        User user = userRepository.findByUserEmail("buyer@test.com")
                .orElseThrow(() -> new RuntimeException("Test data not initialized properly"));

        // Create company request
        CompanyRequest request = new CompanyRequest();
        request.setCompanyName("Test Company");
        request.setCompanyAddress("123 Company St, Business City, 54321");
        request.setCompanyIndustryCategory("Technology");
        request.setCompanyCommercialRegister("CR12345");
        request.setCompanyBusinessLicenseNumber("BL67890");
        request.setCompanyFiscalIdentifier("FI11223");

        // Send the request
        ResponseEntity<CompanyResponse> response = restTemplate.postForEntity(
                buildUrl("/api/companies/user/" + user.getUserId()),
                request,
                CompanyResponse.class);

        // Assert the response
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getCompanyId());
        assertEquals("Test Company", response.getBody().getCompanyName());
        assertEquals("Technology", response.getBody().getCompanyIndustryCategory());
        assertEquals(user.getUserId(), response.getBody().getUserId());
        assertEquals(VerificationStatus.PENDING, response.getBody().getCompanyVerificationStatus());
        assertFalse(response.getBody().isCompanyIsVerified());
    }

    @Test
    public void testUpdateCompanyVerificationStatus() {
        // First create a company
        User user = userRepository.findByUserEmail("seller@test.com")
                .orElseThrow(() -> new RuntimeException("Test data not initialized properly"));

        CompanyRequest createRequest = new CompanyRequest();
        createRequest.setCompanyName("Verification Test Company");
        createRequest.setCompanyAddress("111 Verify St, Test City, 54321");
        createRequest.setCompanyIndustryCategory("Finance");
        createRequest.setCompanyCommercialRegister("CR11111");

        ResponseEntity<CompanyResponse> createResponse = restTemplate.postForEntity(
                buildUrl("/api/companies/user/" + user.getUserId()),
                createRequest,
                CompanyResponse.class);

        Integer companyId = createResponse.getBody().getCompanyId();

        // Update verification status to VERIFIED
        ResponseEntity<CompanyResponse> updateResponse = restTemplate.exchange(
                buildUrl("/api/companies/" + companyId + "/status?status=VERIFIED"),
                HttpMethod.PATCH,
                null,
                CompanyResponse.class);

        // Assert the response
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertNotNull(updateResponse.getBody());
        assertEquals(companyId, updateResponse.getBody().getCompanyId());
        assertEquals(VerificationStatus.VERIFIED, updateResponse.getBody().getCompanyVerificationStatus());
        assertTrue(updateResponse.getBody().isCompanyIsVerified());
    }
}