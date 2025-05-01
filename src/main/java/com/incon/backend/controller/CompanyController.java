package com.incon.backend.controller;

import com.incon.backend.dto.request.CompanyRequest;
import com.incon.backend.dto.response.ApiResponse;
import com.incon.backend.dto.response.CompanyResponse;
import com.incon.backend.enums.VerificationStatus;
import com.incon.backend.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping("/user/{userId}")
    public ResponseEntity<CompanyResponse> createCompany(
            @PathVariable Integer userId,
            @Valid @RequestBody CompanyRequest companyRequest) {
        CompanyResponse response = companyService.createCompany(userId, companyRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<CompanyResponse> getCompanyById(@PathVariable Integer companyId) {
        CompanyResponse response = companyService.getCompanyById(companyId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CompanyResponse> getCompanyByUserId(@PathVariable Integer userId) {
        CompanyResponse response = companyService.getCompanyByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CompanyResponse>> getAllCompanies() {
        List<CompanyResponse> responses = companyService.getAllCompanies();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<CompanyResponse>> getCompaniesByVerificationStatus(
            @PathVariable VerificationStatus status) {
        List<CompanyResponse> responses = companyService.getCompaniesByVerificationStatus(status);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<CompanyResponse>> getCompaniesByIndustryCategory(
            @PathVariable String category) {
        List<CompanyResponse> responses = companyService.getCompaniesByIndustryCategory(category);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{companyId}")
    public ResponseEntity<CompanyResponse> updateCompany(
            @PathVariable Integer companyId,
            @Valid @RequestBody CompanyRequest companyRequest) {
        CompanyResponse response = companyService.updateCompany(companyId, companyRequest);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{companyId}/status")
    public ResponseEntity<CompanyResponse> updateCompanyVerificationStatus(
            @PathVariable Integer companyId,
            @RequestParam VerificationStatus status) {
        CompanyResponse response = companyService.updateCompanyVerificationStatus(companyId, status);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{companyId}")
    public ResponseEntity<ApiResponse> deleteCompany(@PathVariable Integer companyId) {
        companyService.deleteCompany(companyId);
        return ResponseEntity.ok(new ApiResponse("Company deleted successfully", HttpStatus.OK));
    }
}