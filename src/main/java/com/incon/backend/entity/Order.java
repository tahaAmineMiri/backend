package com.incon.backend.entity;

import com.incon.backend.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date orderDate;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    private String shippingDetails;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private Buyer buyer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(mappedBy = "order")
    private Payment payment;

    @Version
    private Integer version;

    // Methods from class diagram
    public void updateStatus(OrderStatus status) {
        this.status = status;
    }

    public void cancelOrder() {
        if (this.status == OrderStatus.PENDING || this.status == OrderStatus.PROCESSING) {
            this.status = OrderStatus.CANCELLED;
        } else {
            throw new IllegalStateException("Cannot cancel order in status: " + this.status);
        }
    }
}