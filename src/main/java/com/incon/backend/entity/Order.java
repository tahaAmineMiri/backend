package com.incon.backend.entity;

import com.incon.backend.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;

    private String orderNumber = UUID.randomUUID().toString();

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private Buyer orderBuyer;

    @OneToOne(mappedBy = "paymentOrder")
    private Payment orderPayment;

    @OneToMany(mappedBy = "orderItemOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    private LocalDateTime orderDate = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus = OrderStatus.PENDING;

    @Column(nullable = false)
    private BigDecimal orderTotalAmount = BigDecimal.ZERO;

    private String orderShippingAddress;

    private String orderBillingAddress;

    public void updateTotalAmount() {
        this.orderTotalAmount = orderItems.stream()
                .map(item -> item.getOrderItemPrice().multiply(BigDecimal.valueOf(item.getOrderItemQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrderItemOrder(this);
        updateTotalAmount();
    }

    public void removeOrderItem(OrderItem orderItem) {
        orderItems.remove(orderItem);
        orderItem.setOrderItemOrder(null);
        updateTotalAmount();
    }
}