package com.incon.backend.service.impl;

import com.incon.backend.dto.request.BuyerRequest;
import com.incon.backend.dto.response.BuyerResponse;
import com.incon.backend.entity.Buyer;
import com.incon.backend.entity.Cart;
import com.incon.backend.exception.BadRequestException;
import com.incon.backend.exception.ResourceNotFoundException;
import com.incon.backend.mapper.BuyerMapper;
import com.incon.backend.repository.BuyerRepository;
import com.incon.backend.repository.CartRepository;
import com.incon.backend.service.BuyerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BuyerServiceImpl implements BuyerService {

    private final BuyerRepository buyerRepository;
    private final CartRepository cartRepository;
    private final BuyerMapper buyerMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public BuyerResponse registerBuyer(BuyerRequest buyerRequest) {
        // Check if email already exists
        if (buyerRepository.existsByUserEmail(buyerRequest.getUserEmail())) {
            throw new BadRequestException("Email is already registered");
        }

        // Convert buyerRequest to entity
        Buyer buyer = buyerMapper.toBuyer(buyerRequest);

        // Encode password
        buyer.setUserPassword(passwordEncoder.encode(buyerRequest.getUserPassword()));

        // Save buyer
        Buyer savedBuyer = buyerRepository.save(buyer);

        // Create and associate a cart
        Cart cart = new Cart();
        cart.setCartBuyer(savedBuyer);
        cart.setCartTotalAmount(BigDecimal.ZERO);
        cartRepository.save(cart);

        savedBuyer.setBuyerCart(cart);
        buyerRepository.save(savedBuyer);

        return buyerMapper.toBuyerResponse(savedBuyer);
    }

    @Override
    @Transactional(readOnly = true)
    public BuyerResponse getBuyerById(Integer buyerId) {
        Buyer buyer = getBuyerEntityById(buyerId);
        return buyerMapper.toBuyerResponse(buyer);
    }

    @Override
    @Transactional(readOnly = true)
    public BuyerResponse getBuyerByEmail(String userEmail) {
        Buyer buyer = buyerRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found with email: " + userEmail));
        return buyerMapper.toBuyerResponse(buyer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BuyerResponse> getAllBuyers() {
        return buyerRepository.findAll().stream()
                .map(buyerMapper::toBuyerResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BuyerResponse updateBuyer(Integer buyerId, BuyerRequest buyerRequest) {
        Buyer existingBuyer = getBuyerEntityById(buyerId);

        // Check if email is already taken by another user
        if (!existingBuyer.getUserEmail().equals(buyerRequest.getUserEmail()) &&
                buyerRepository.existsByUserEmail(buyerRequest.getUserEmail())) {
            throw new BadRequestException("Email is already taken");
        }

        // Update buyer information
        existingBuyer.setUserEmail(buyerRequest.getUserEmail());
        existingBuyer.setUserFullName(buyerRequest.getUserFullName());
        existingBuyer.setUserPosition(buyerRequest.getUserPosition());
        existingBuyer.setUserBusinessPhone(buyerRequest.getUserBusinessPhone());

        // Update password if provided
        if (buyerRequest.getUserPassword() != null && !buyerRequest.getUserPassword().isEmpty()) {
            existingBuyer.setUserPassword(passwordEncoder.encode(buyerRequest.getUserPassword()));
        }

        // Save updated buyer
        Buyer updatedBuyer = buyerRepository.save(existingBuyer);

        return buyerMapper.toBuyerResponse(updatedBuyer);
    }

    @Override
    @Transactional
    public void deleteBuyer(Integer buyerId) {
        Buyer buyer = getBuyerEntityById(buyerId);
        buyerRepository.delete(buyer);
    }

    @Override
    @Transactional(readOnly = true)
    public Buyer getBuyerEntityById(Integer buyerId) {
        return buyerRepository.findById(buyerId)
                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found with id: " + buyerId));
    }
}