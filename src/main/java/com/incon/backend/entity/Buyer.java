package com.incon.backend.entity;

import com.incon.backend.enums.OrderStatus;
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
@SuperBuilder // Add this to work with the parent class's SuperBuilder
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("BUYER")
public class Buyer extends User {

    @OneToOne(mappedBy = "buyer")
    private Cart cart;

    @OneToMany(mappedBy = "buyer")
    private List<Order> orders = new ArrayList<>();

    public void addToCart(Product product, int quantity) {
        if (cart == null) {
            cart = new Cart();
            cart.setBuyer(this);
        }
        cart.addProduct(product, quantity);
    }

    public Order placeOrder() {
        if (cart == null || cart.getCartItems().isEmpty()) {
            throw new IllegalStateException("Cannot place order with empty cart");
        }

        Order order = new Order();
        order.setBuyer(this);
        order.setOrderDate(new java.util.Date());
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(cart.getTotalAmount());

        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setItemPrice(cartItem.getItemPrice());
            orderItem.setSubtotal(cartItem.getSubtotal());
            order.getOrderItems().add(orderItem);
        }

        cart.getCartItems().clear();
        cart.setTotalAmount(0.0f);

        return order;
    }
}