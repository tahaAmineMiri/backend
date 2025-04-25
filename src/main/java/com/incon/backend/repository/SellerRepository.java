package com.incon.backend.repository;

import com.incon.backend.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Integer> {
    // Find a seller by their email address
    Optional<Seller> findByUserEmail(String userEmail);

    // Check if a seller exists by their email address
    boolean existsByUserEmail(String userEmail);
}