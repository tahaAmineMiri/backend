package com.incon.backend.repository;

import com.incon.backend.entity.Buyer;
import com.incon.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Long> {

    Optional<Buyer> findByBuyerId(Buyer buyer);

    Optional<Buyer> findByBuyerId(Integer buyerId);
}