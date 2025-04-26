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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private Buyer cartBuyer;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cartTotalAmount = BigDecimal.ZERO;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime cartCreatedAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime cartUpdatedAt;

    @OneToMany(mappedBy = "cartItemCart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    public void updateTotalAmount() {
        this.cartTotalAmount = cartItems.stream()
                .map(CartItem::getCartItemSubtotal)  // Use existing subtotal instead
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void addCartItem(CartItem cartItem) {
        cartItems.add(cartItem);
        cartItem.setCartItemCart(this);
        updateTotalAmount();
    }

    public void removeCartItem(CartItem cartItem) {
        cartItems.remove(cartItem);
        cartItem.setCartItemCart(null);
        updateTotalAmount();
    }
}