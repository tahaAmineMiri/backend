package com.incon.backend.repository;

import com.incon.backend.entity.Product;
import com.incon.backend.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    // Find all products by category
    List<Product> findByProductCategory(String productCategory);

    // Find products by seller
    List<Product> findByProductSeller(Seller productSeller);

    // Find products with stock less than a specific quantity
    List<Product> findByProductStockQuantityLessThan(int quantity);

    // Find products by price range
    List<Product> findByProductPriceBetween(java.math.BigDecimal minPrice, java.math.BigDecimal maxPrice);

    // Find products by name containing a keyword (case-insensitive)
    List<Product> findByProductNameContainingIgnoreCase(String keyword);
}