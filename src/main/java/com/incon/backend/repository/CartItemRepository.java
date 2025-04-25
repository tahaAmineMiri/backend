package com.incon.backend.repository;

import com.incon.backend.entity.Cart;
import com.incon.backend.entity.CartItem;
import com.incon.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    // Find cart item by cart
    List<CartItem> findByCart(Cart cart);
    // Find cart item by cart id
    List<CartItem> findByCartCartId(Integer cartId);
    // Find cart item by cart and product id
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
}