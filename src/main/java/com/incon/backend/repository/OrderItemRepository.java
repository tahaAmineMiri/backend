package com.incon.backend.repository;

import com.incon.backend.entity.Order;
import com.incon.backend.entity.OrderItem;
import com.incon.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    List<OrderItem> findByOrder(Order order);
    List<OrderItem> findByOrderId(Long orderId);
    List<OrderItem> findByProduct(Product product);
    List<OrderItem> findByProductId(Long productId);
}