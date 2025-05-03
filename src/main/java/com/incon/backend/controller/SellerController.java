package com.incon.backend.controller;

import com.incon.backend.dto.request.SellerRequest;
import com.incon.backend.dto.response.ApiResponse;
import com.incon.backend.dto.response.SellerResponse;
import com.incon.backend.service.SellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sellers")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;

    @PostMapping
    public ResponseEntity<SellerResponse> registerSeller(@Valid @RequestBody SellerRequest sellerRequest) {
        SellerResponse response = sellerService.registerSeller(sellerRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SellerResponse> getSellerById(@PathVariable("id") Integer sellerId) {
        SellerResponse response = sellerService.getSellerById(sellerId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<SellerResponse> getSellerByEmail(@PathVariable String email) {
        SellerResponse response = sellerService.getSellerByEmail(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SellerResponse>> getAllSellers() {
        List<SellerResponse> responses = sellerService.getAllSellers();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN') or @securityService.isSeller(#sellerId)")
    public ResponseEntity<SellerResponse> updateSeller(
            @PathVariable("id") Integer sellerId,
            @Valid @RequestBody SellerRequest sellerRequest) {
        SellerResponse response = sellerService.updateSeller(sellerId, sellerRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN') or @securityService.isSeller(#sellerId)")
    public ResponseEntity<ApiResponse> deleteSeller(@PathVariable("id") Integer sellerId) {
        sellerService.deleteSeller(sellerId);
        return ResponseEntity.ok(new ApiResponse("Seller deleted successfully", HttpStatus.OK));
    }
}