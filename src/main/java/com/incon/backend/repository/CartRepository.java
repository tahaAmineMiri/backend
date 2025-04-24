package com.incon.backend.repository;

import com.incon.backend.entity.Buyer;
import com.incon.backend.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    // Find cart by buyer
    Optional<Cart> findByBuyer(Buyer buyer);
    // Find by bayer id
    Optional<Cart> findByBuyerId(Integer buyerId);
}