package com.incon.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartId;

    @Column(nullable = false)
    private Float totalAmount = 0.0f;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @OneToOne
    @JoinColumn(name = "buyer_id")
    private Buyer buyer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    // Methods from class diagram
    public void addProduct(Product product, int quantity) {
        // Check if product already exists in cart
        for (CartItem item : cartItems) {
            if (item.getProduct().getProductId().equals(product.getProductId())) {
                // Update quantity if product exists
                item.setQuantity(item.getQuantity() + quantity);
                item.setSubtotal(item.getItemPrice() * item.getQuantity());
                updateTotalAmount();
                return;
            }
        }

        // Add new cart item if product not already in cart
        CartItem cartItem = new CartItem();
        cartItem.setCart(this);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.setItemPrice(product.getPrice());
        cartItem.setSubtotal(product.getPrice() * quantity);
        cartItems.add(cartItem);

        updateTotalAmount();
    }

    public void removeProduct(Product product) {
        cartItems.removeIf(item -> item.getProduct().getProductId().equals(product.getProductId()));
        updateTotalAmount();
    }

    public void checkout() {
        // To be implemented in service layer
    }

    private void updateTotalAmount() {
        totalAmount = 0.0f;
        for (CartItem item : cartItems) {
            totalAmount += item.getSubtotal();
        }
    }
}