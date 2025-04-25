package com.incon.backend.repository;

import com.incon.backend.entity.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Integer> {
    Optional<Buyer> findById(Integer id);
    Optional<Buyer> findByEmail(String email);
}