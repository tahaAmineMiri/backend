package com.incon.backend.repository;

import com.incon.backend.entity.Buyer;
import com.incon.backend.entity.Order;
import com.incon.backend.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByBuyer(Buyer buyer);
    List<Order> findByStatus(OrderStatus status);
    List<Order> findByOrderDateBetween(Date startDate, Date endDate);
}