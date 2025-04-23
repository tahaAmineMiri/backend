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

    @OneToOne(mappedBy = "buyer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    public void assignCart(Cart cart) {
        this.cart = cart;
        if (cart != null) {
            cart.setBuyer(this);
        }
    }

    public void addOrder(Order order) {
        orders.add(order);
        order.setBuyer(this);
    }
}