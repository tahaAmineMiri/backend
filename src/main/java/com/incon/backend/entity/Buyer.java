package com.incon.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("BUYER")
public class Buyer extends User {

    @OneToOne(mappedBy = "cartBuyer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart buyerCart;

    @OneToMany(mappedBy = "orderBuyer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> buyerOrders = new ArrayList<>();

    public void assignCart(Cart cart) {
        this.buyerCart = cart;
        if (cart != null) {
            cart.setCartBuyer(this);
        }
    }

    public void addOrder(Order order) {
        buyerOrders.add(order);
        order.setOrderBuyer(this);
    }
}