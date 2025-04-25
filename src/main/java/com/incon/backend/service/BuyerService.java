package com.incon.backend.service;

import com.incon.backend.dto.request.BuyerRequest;
import com.incon.backend.dto.response.BuyerResponse;
import com.incon.backend.entity.Buyer;

import java.util.List;

public interface BuyerService {
    BuyerResponse registerBuyer(BuyerRequest request);
    BuyerResponse getBuyerById(Integer buyerId);
    BuyerResponse getBuyerByEmail(String userEmail);
    List<BuyerResponse> getAllBuyers();
    BuyerResponse updateBuyer(Integer buyerId, BuyerRequest request);
    void deleteBuyer(Integer buyerId);
    Buyer getBuyerEntityById(Integer buyerId);
}