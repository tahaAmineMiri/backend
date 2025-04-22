package com.incon.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartItemId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Float itemPrice;

    @Column(nullable = false)
    private Float subtotal;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    // Method from class diagram
    public void updateQuantity(int quantity) {
        this.quantity = quantity;
        this.subtotal = this.itemPrice * quantity;

        // Update cart total
        if (cart != null) {
            float newTotal = 0.0f;
            for (CartItem item : cart.getCartItems()) {
                newTotal += item.getSubtotal();
            }
            cart.setTotalAmount(newTotal);
        }
    }
}