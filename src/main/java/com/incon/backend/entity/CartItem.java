package com.incon.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cartItemCart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product cartItemProduct;

    @Column(nullable = false)
    private Integer cartItemQuantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cartItemPrice;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cartItemSubtotal;

    public void updateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }
        this.cartItemQuantity = quantity;
        this.cartItemSubtotal = this.cartItemPrice.multiply(BigDecimal.valueOf(quantity));
        if (cartItemCart != null) {
            cartItemCart.updateTotalAmount();
        }
    }
}