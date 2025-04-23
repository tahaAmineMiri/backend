package com.incon.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private Buyer buyer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    public void addProduct(Product product, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive.");

        for (CartItem item : cartItems) {
            if (item.getProduct().getProductId().equals(product.getProductId())) {
                item.updateQuantity(item.getQuantity() + quantity);
                return;
            }
        }

        CartItem cartItem = new CartItem();
        cartItem.setCart(this);
        cartItem.setProduct(product);
        cartItem.setItemPrice(product.getPrice());
        cartItem.setQuantity(quantity);
        cartItem.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        cartItems.add(cartItem);

        updateTotalAmount();
    }

    public void removeProduct(Product product) {
        cartItems.removeIf(item -> item.getProduct().getProductId().equals(product.getProductId()));
        updateTotalAmount();
    }

    protected void updateTotalAmount() {
        this.totalAmount = cartItems.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
