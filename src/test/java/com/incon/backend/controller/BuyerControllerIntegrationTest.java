package com.incon.backend.controller;

import com.incon.backend.dto.request.BuyerRequest;
import com.incon.backend.dto.response.BuyerResponse;
import com.incon.backend.entity.Buyer;
import com.incon.backend.enums.Role;
import com.incon.backend.integration.BaseIntegrationTest;
import com.incon.backend.repository.BuyerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

public class BuyerControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private BuyerRepository buyerRepository;

    @Test
    public void testRegisterBuyer() {
        // Create a request
        BuyerRequest request = new BuyerRequest();
        request.setUserEmail("newbuyer@test.com");
        request.setUserPassword("password123");
        request.setUserFullName("New Buyer");
        request.setUserPosition("Manager");
        request.setUserBusinessPhone("1122334455");
        request.setUserRole(Role.BUYER);

        // Send the request
        ResponseEntity<BuyerResponse> response = restTemplate.postForEntity(
                buildUrl("/api/buyers"), request, BuyerResponse.class);

        // Assert the response
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getUserId());
        assertEquals("newbuyer@test.com", response.getBody().getUserEmail());
        assertEquals("New Buyer", response.getBody().getUserFullName());

        // Verify the buyer was persisted
        assertTrue(buyerRepository.existsByUserEmail("newbuyer@test.com"));
    }

    @Test
    public void testGetBuyerById() {
        // Get an existing buyer
        Buyer existingBuyer = buyerRepository.findByUserEmail("buyer@test.com")
                .orElseThrow(() -> new RuntimeException("Test data not initialized properly"));

        // Get the buyer by ID
        ResponseEntity<BuyerResponse> response = restTemplate.getForEntity(
                buildUrl("/api/buyers/" + existingBuyer.getUserId()), BuyerResponse.class);

        // Assert the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(existingBuyer.getUserId(), response.getBody().getUserId());
        assertEquals(existingBuyer.getUserEmail(), response.getBody().getUserEmail());
        assertEquals(existingBuyer.getUserFullName(), response.getBody().getUserFullName());
    }

    @Test
    public void testUpdateBuyer() {
        // Get an existing buyer
        Buyer existingBuyer = buyerRepository.findByUserEmail("buyer@test.com")
                .orElseThrow(() -> new RuntimeException("Test data not initialized properly"));

        // Create update request
        BuyerRequest request = new BuyerRequest();
        request.setUserEmail("buyer@test.com"); // Keep the same email
        request.setUserPassword("newpassword");
        request.setUserFullName("Updated Buyer Name");
        request.setUserPosition("Senior Manager");
        request.setUserBusinessPhone("5566778899");
        request.setUserRole(Role.BUYER);

        // Send the update request
        ResponseEntity<BuyerResponse> response = restTemplate.exchange(
                buildUrl("/api/buyers/" + existingBuyer.getUserId()),
                HttpMethod.PUT,
                new HttpEntity<>(request),
                BuyerResponse.class);

        // Assert the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(existingBuyer.getUserId(), response.getBody().getUserId());
        assertEquals("buyer@test.com", response.getBody().getUserEmail());
        assertEquals("Updated Buyer Name", response.getBody().getUserFullName());
        assertEquals("Senior Manager", response.getBody().getUserPosition());
        assertEquals("5566778899", response.getBody().getUserBusinessPhone());

        // Verify the update was persisted
        Buyer updatedBuyer = buyerRepository.findById(existingBuyer.getUserId()).orElse(null);
        assertNotNull(updatedBuyer);
        assertEquals("Updated Buyer Name", updatedBuyer.getUserFullName());
    }

    @Test
    public void testDeleteBuyer() {
        // Create a buyer to delete
        Buyer buyer = new Buyer();
        buyer.setUserEmail("tobedeleted@test.com");
        buyer.setUserPassword(passwordEncoder.encode("password"));
        buyer.setUserFullName("To Be Deleted");
        buyer.setUserPosition("Temporary");
        buyer.setUserBusinessPhone("9999999999");
        buyer.setUserRole(Role.BUYER);

        Buyer savedBuyer = buyerRepository.save(buyer);

        // Send delete request
        restTemplate.delete(buildUrl("/api/buyers/" + savedBuyer.getUserId()));

        // Verify the buyer was deleted
        assertFalse(buyerRepository.existsById(savedBuyer.getUserId()));
    }
}