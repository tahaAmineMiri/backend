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
    // Find cart items by cart
    List<CartItem> findByCartItemCart(Cart cartItemCart);

    // Find cart items by cart id
    List<CartItem> findByCartItemCartCartId(Integer cartId);

    // Find cart item by cart and product
    Optional<CartItem> findByCartItemCartAndCartItemProduct(Cart cartItemCart, Product cartItemProduct);
}