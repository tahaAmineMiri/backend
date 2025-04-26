//package com.incon.backend.service.impl;
//
//import com.incon.backend.dto.request.SellerRequest;
//import com.incon.backend.dto.response.SellerResponse;
//import com.incon.backend.entity.Seller;
//import com.incon.backend.exception.BadRequestException;
//import com.incon.backend.exception.ResourceNotFoundException;
//import com.incon.backend.mapper.SellerMapper;
//import com.incon.backend.repository.SellerRepository;
//import com.incon.backend.service.SellerService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class SellerServiceImpl implements SellerService {
//
//    private final SellerRepository sellerRepository;
//    private final SellerMapper sellerMapper;
//    private final PasswordEncoder passwordEncoder;
//
//    @Override
//    @Transactional
//    public SellerResponse registerSeller(SellerRequest sellerRequest) {
//        // Check if email already exists
//        if (sellerRepository.existsByUserEmail(sellerRequest.getUserEmail())) {
//            throw new BadRequestException("Email is already registered");
//        }
//
//        // Convert sellerRequest to entity
//        Seller seller = sellerMapper.toSeller(sellerRequest);
//
//        // Encode password
//        seller.setUserPassword(passwordEncoder.encode(sellerRequest.getUserPassword()));
//
//        // Save seller
//        Seller savedSeller = sellerRepository.save(seller);
//
//        return sellerMapper.toSellerResponse(savedSeller);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public SellerResponse getSellerById(Integer sellerId) {
//        Seller seller = getSellerEntityById(sellerId);
//        return sellerMapper.toSellerResponse(seller);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public SellerResponse getSellerByEmail(String userEmail) {
//        Seller seller = sellerRepository.findByUserEmail(userEmail)
//                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with email: " + userEmail));
//        return sellerMapper.toSellerResponse(seller);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<SellerResponse> getAllSellers() {
//        return sellerRepository.findAll().stream()
//                .map(sellerMapper::toSellerResponse)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    @Transactional
//    public SellerResponse updateSeller(Integer sellerId, SellerRequest sellerRequest) {
//        Seller existingSeller = getSellerEntityById(sellerId);
//
//        // Check if email is already taken by another user
//        if (!existingSeller.getUserEmail().equals(sellerRequest.getUserEmail()) &&
//                sellerRepository.existsByUserEmail(sellerRequest.getUserEmail())) {
//            throw new BadRequestException("Email is already taken");
//        }
//
//        // Update seller information
//        existingSeller.setUserEmail(sellerRequest.getUserEmail());
//        existingSeller.setUserFullName(sellerRequest.getUserFullName());
//        existingSeller.setUserPosition(sellerRequest.getUserPosition());
//        existingSeller.setUserBusinessPhone(sellerRequest.getUserBusinessPhone());
//
//        // Update password if provided
//        if (sellerRequest.getUserPassword() != null && !sellerRequest.getUserPassword().isEmpty()) {
//            existingSeller.setUserPassword(passwordEncoder.encode(sellerRequest.getUserPassword()));
//        }
//
//        // Save updated seller
//        Seller updatedSeller = sellerRepository.save(existingSeller);
//
//        return sellerMapper.toSellerResponse(updatedSeller);
//    }
//
//    @Override
//    @Transactional
//    public void deleteSeller(Integer sellerId) {
//        Seller seller = getSellerEntityById(sellerId);
//        sellerRepository.delete(seller);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public Seller getSellerEntityById(Integer sellerId) {
//        return sellerRepository.findById(sellerId)
//                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with id: " + sellerId));
//    }
//}