package com.incon.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderItemId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order orderItemOrder;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product orderItemProduct;

    @Column(nullable = false)
    private Integer orderItemQuantity;

    @Column(nullable = false)
    private BigDecimal orderItemPrice;

    @Column(nullable = false)
    private BigDecimal orderItemSubtotal;

    public void updateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }
        this.orderItemQuantity = quantity;
        this.orderItemSubtotal = this.orderItemPrice.multiply(BigDecimal.valueOf(quantity));
    }
}