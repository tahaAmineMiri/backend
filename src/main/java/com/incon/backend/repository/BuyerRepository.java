package com.incon.backend.repository;

import com.incon.backend.entity.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Integer> {
    Optional<Buyer> findByUserEmail(String userEmail);
    boolean existsByUserEmail(String userEmail);
}