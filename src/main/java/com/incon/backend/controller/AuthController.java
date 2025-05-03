package com.incon.backend.controller;

import com.incon.backend.dto.request.BuyerRequest;
import com.incon.backend.dto.request.LoginRequest;
import com.incon.backend.dto.request.SellerRequest;
import com.incon.backend.dto.response.BuyerResponse;
import com.incon.backend.dto.response.LoginResponse;
import com.incon.backend.dto.response.SellerResponse;
import com.incon.backend.entity.User;
import com.incon.backend.enums.Role;
import com.incon.backend.mapper.UserMapper;
import com.incon.backend.repository.UserRepository;
import com.incon.backend.service.BuyerService;
import com.incon.backend.service.JwtService;
import com.incon.backend.service.SellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final BuyerService buyerService;
    private final SellerService sellerService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtService.generateToken(userDetails);

        User user = userRepository.findByUserEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(LoginResponse.builder()
                .token(jwt)
                .expiresIn(86400000L) // 24 hours in milliseconds
                .user(userMapper.toUserResponse(user))
                .build());
    }

    @PostMapping("/register/buyer")
    public ResponseEntity<BuyerResponse> registerBuyer(@Valid @RequestBody BuyerRequest request) {
        request.setUserRole(Role.BUYER);
        BuyerResponse response = buyerService.registerBuyer(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/register/seller")
    public ResponseEntity<SellerResponse> registerSeller(@Valid @RequestBody SellerRequest request) {
        request.setUserRole(Role.SELLER);
        SellerResponse response = sellerService.registerSeller(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}