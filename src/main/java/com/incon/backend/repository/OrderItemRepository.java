package com.incon.backend.repository;

import com.incon.backend.entity.Order;
import com.incon.backend.entity.OrderItem;
import com.incon.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    // Change this method:
    List<OrderItem> findByOrderItemOrder(Order order);  // Instead of findByOrder

    // Change this method too:
    List<OrderItem> findByOrderItemOrderOrderId(Integer orderId);  // Instead of findByOrderOrderId

    // These methods also need to be updated:
    List<OrderItem> findByOrderItemProduct(Product product);  // Instead of findByProduct
    List<OrderItem> findByOrderItemProductProductId(Integer productId);  // Instead of findByProductProductId
}