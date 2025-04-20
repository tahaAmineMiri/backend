package com.incon.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;


import java.util.Objects;


@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {

    // Getters and Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;

    @NotBlank(message = "Product name is required")
    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Positive(message = "Price must be greater than zero")
    @Column(nullable = false)
    private float price;

    @Min(value = 0, message = "Stock quantity cannot be negative")
    private int stockQuantity;

    @NotBlank(message = "Category is required")
    @Column(nullable = false)
    private String category;

    @NotBlank(message = "Image URL is required")
    @Column(nullable = false)
    private String image;

    @DecimalMin(value = "0.0", inclusive = true, message = "Rating cannot be negative")
    @DecimalMax(value = "5.0", inclusive = true, message = "Rating cannot exceed 5")
    private float rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;


    public Product() {
    }

    public Product(String name, String description, float price, int stockQuantity,
                   String category, String image, Seller seller) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
        this.image = image;
        this.rating = 0.0f; // Default rating for new products
        this.seller = seller;
    }

    // Business methods
    public void updateStock(int newQuantity) {
        this.stockQuantity = newQuantity;
    }

    public void updatePrice(float newPrice) {
        this.price = newPrice;
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return productId == product.productId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                ", category='" + category + '\'' +
                '}';
    }
}