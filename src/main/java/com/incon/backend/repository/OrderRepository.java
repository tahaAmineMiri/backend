package com.incon.backend.repository;

import com.incon.backend.entity.Buyer;
import com.incon.backend.entity.Order;
import com.incon.backend.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByBuyer(Buyer buyer);
    List<Order> findByBuyerId(Long buyerId);
    List<Order> findByStatus(OrderStatus status);
    List<Order> findByOrderDateBetween(LocalDateTime start, LocalDateTime end);
    Optional<Order> findByOrderNumber(String orderNumber);
}