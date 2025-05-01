package com.incon.backend.repository;

import com.incon.backend.entity.Product;
import com.incon.backend.entity.Review;
import com.incon.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByReviewUser(User user);

    // Fix this method name
    List<Review> findByReviewUser_UserId(Integer userId);

    List<Review> findByReviewProduct(Product product);

    // Fix this method name
    List<Review> findByReviewProduct_ProductId(Integer productId);

    List<Review> findByReviewRating(Integer rating);
    List<Review> findByReviewDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Fix this method name
    List<Review> findByReviewProduct_ProductIdAndReviewUser_UserId(Integer productId, Integer userId);
}