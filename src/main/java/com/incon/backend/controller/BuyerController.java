package com.incon.backend.controller;

import com.incon.backend.dto.request.BuyerRequest;
import com.incon.backend.dto.response.ApiResponse;
import com.incon.backend.dto.response.BuyerResponse;
import com.incon.backend.service.BuyerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buyers")
@RequiredArgsConstructor
public class BuyerController {

    private final BuyerService buyerService;

    @PostMapping
    public ResponseEntity<BuyerResponse> registerBuyer(@Valid @RequestBody BuyerRequest buyerRequest) {
        BuyerResponse response = buyerService.registerBuyer(buyerRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuyerResponse> getBuyerById(@PathVariable("id") Integer buyerId) {
        BuyerResponse response = buyerService.getBuyerById(buyerId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<BuyerResponse> getBuyerByEmail(@PathVariable String email) {
        BuyerResponse response = buyerService.getBuyerByEmail(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<BuyerResponse>> getAllBuyers() {
        List<BuyerResponse> responses = buyerService.getAllBuyers();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BuyerResponse> updateBuyer(
            @PathVariable("id") Integer buyerId,
            @Valid @RequestBody BuyerRequest buyerRequest) {
        BuyerResponse response = buyerService.updateBuyer(buyerId, buyerRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteBuyer(@PathVariable("id") Integer buyerId) {
        buyerService.deleteBuyer(buyerId);
        return ResponseEntity.ok(new ApiResponse("Buyer deleted successfully", HttpStatus.OK));
    }
}