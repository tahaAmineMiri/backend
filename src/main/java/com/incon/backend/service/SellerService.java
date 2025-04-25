package com.incon.backend.service;

import com.incon.backend.dto.request.SellerRequest;
import com.incon.backend.dto.response.SellerResponse;
import com.incon.backend.entity.Seller;

import java.util.List;

public interface SellerService {

    /**
     * Register a new seller
     *
     * @param request The seller registration request
     * @return The registered seller response
     */
    SellerResponse registerSeller(SellerRequest request);

    /**
     * Find a seller by ID
     *
     * @param sellerId The seller ID
     * @return The seller response
     */
    SellerResponse getSellerById(Integer sellerId);

    /**
     * Find a seller by email
     *
     * @param userEmail The seller email
     * @return The seller response
     */
    SellerResponse getSellerByEmail(String userEmail);

    /**
     * Get all sellers
     *
     * @return List of seller responses
     */
    List<SellerResponse> getAllSellers();

    /**
     * Update seller information
     *
     * @param sellerId The seller ID
     * @param request The seller registration request with updated information
     * @return The updated seller response
     */
    SellerResponse updateSeller(Integer sellerId, SellerRequest request);

    /**
     * Delete a seller by ID
     *
     * @param sellerId The seller ID
     */
    void deleteSeller(Integer sellerId);

    /**
     * Get seller entity by ID (for internal use)
     *
     * @param sellerId The seller ID
     * @return The seller entity
     */
    Seller getSellerEntityById(Integer sellerId);
}