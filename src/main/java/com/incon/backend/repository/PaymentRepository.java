package com.incon.backend.repository;

import com.incon.backend.entity.Order;
import com.incon.backend.entity.Payment;
import com.incon.backend.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findByOrder(Order order);
    List<Payment> findByStatus(PaymentStatus status);
}