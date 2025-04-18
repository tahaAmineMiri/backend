package com.incon.backend.repository;

import com.incon.backend.entity.Product;
//import com.incon.backend.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    // Find all products by category
    List<Product> findByCategory(String category);

    // Find products with stock less than a specific quantity
    List<Product> findByStockQuantityLessThan(int quantity);

    // Find products by price range
    List<Product> findByPriceBetween(float minPrice, float maxPrice);

    // Find products by name containing a keyword (case insensitive)
    List<Product> findByNameContainingIgnoreCase(String keyword);
}