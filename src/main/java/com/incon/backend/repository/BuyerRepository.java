package com.incon.backend.repository;

import com.incon.backend.entity.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Integer> {
    // Find buyer  by email
    Optional<Buyer> findByEmail(String email);
    // Check if buyer exists byemail
    boolean existsByEmail(String email);
}