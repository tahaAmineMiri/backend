package com.incon.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;

    @NotBlank(message = "Product name is required")
    @Column(nullable = false)
    private String productName;

    @Column(columnDefinition = "TEXT")
    private String productDescription;

    @Positive(message = "Price must be greater than zero")
    @Column(nullable = false)
    private BigDecimal productPrice;

    @Min(value = 0, message = "Stock quantity cannot be negative")
    private int productStockQuantity;

    @NotBlank(message = "Category is required")
    @Column(nullable = false)
    private String productCategory;

    @NotBlank(message = "Image URL is required")
    @Column(nullable = false)
    private String productImage;

    @DecimalMin(value = "0.0", inclusive = true, message = "Rating cannot be negative")
    @DecimalMax(value = "5.0", inclusive = true, message = "Rating cannot exceed 5")
    private float productRating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller productSeller;

    @OneToMany(mappedBy = "reviewProduct", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> productReviews = new ArrayList<>();

    // Business methods
    public void updateStock(int newQuantity) {
        this.productStockQuantity = newQuantity;
    }

    public void updatePrice(BigDecimal newPrice) {
        this.productPrice = newPrice;
    }

    public void addReview(Review review) {
        productReviews.add(review);
        review.setReviewProduct(this);
        updateRating();
    }

    public void removeReview(Review review) {
        productReviews.remove(review);
        review.setReviewProduct(null);
        updateRating();
    }

    public void updateRating() {
        if (productReviews.isEmpty()) {
            this.productRating = 0.0f;
            return;
        }

        float sum = 0.0f;
        for (Review review : productReviews) {
            sum += review.getReviewRating();
        }
        this.productRating = sum / productReviews.size();
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return productId.equals(product.getProductId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", productStockQuantity=" + productStockQuantity +
                ", productCategory='" + productCategory + '\'' +
                '}';
    }
}