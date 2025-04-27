package com.incon.backend.repository;

import com.incon.backend.entity.Buyer;
import com.incon.backend.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    // Change this method:
    Optional<Cart> findByCartBuyer(Buyer buyer);  // Instead of findCartByBuyer

    // Change this method too:
    Optional<Cart> findByCartBuyerUserId(Integer buyerId);  // Instead of findCartByBuyerId
}