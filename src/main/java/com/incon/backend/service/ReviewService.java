package com.incon.backend.service;

import com.incon.backend.dto.request.ReviewRequest;
import com.incon.backend.dto.response.ReviewResponse;
import com.incon.backend.entity.Review;

import java.time.LocalDateTime;
import java.util.List;

public interface ReviewService {
    /**
     * Create a new review by a user for a product
     *
     * @param userId The user ID
     * @param reviewRequest The review details
     * @return The review response
     */
    ReviewResponse createReview(Integer userId, ReviewRequest reviewRequest);

    /**
     * Get a review by its ID
     *
     * @param reviewId The review ID
     * @return The review response
     */
    ReviewResponse getReviewById(Integer reviewId);

    /**
     * Get all reviews by a specific user
     *
     * @param userId The user ID
     * @return List of review responses
     */
    List<ReviewResponse> getReviewsByUserId(Integer userId);

    /**
     * Get all reviews for a specific product
     *
     * @param productId The product ID
     * @return List of review responses
     */
    List<ReviewResponse> getReviewsByProductId(Integer productId);

    /**
     * Get all reviews with a specific rating
     *
     * @param rating The rating value
     * @return List of review responses
     */
    List<ReviewResponse> getReviewsByRating(Integer rating);

    /**
     * Get all reviews created within a date range
     *
     * @param startDate The start date
     * @param endDate The end date
     * @return List of review responses
     */
    List<ReviewResponse> getReviewsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Update a review
     *
     * @param reviewId The review ID
     * @param userId The user ID (for verification)
     * @param reviewRequest The updated review details
     * @return The updated review response
     */
    ReviewResponse updateReview(Integer reviewId, Integer userId, ReviewRequest reviewRequest);

    /**
     * Delete a review
     *
     * @param reviewId The review ID
     * @param userId The user ID (for verification)
     */
    void deleteReview(Integer reviewId, Integer userId);

    /**
     * Check if a user has already reviewed a product
     *
     * @param userId The user ID
     * @param productId The product ID
     * @return true if the user has already reviewed the product, false otherwise
     */
    boolean hasUserReviewedProduct(Integer userId, Integer productId);

    /**
     * Get review entity by ID (for internal use)
     *
     * @param reviewId The review ID
     * @return The review entity
     */
    Review getReviewEntityById(Integer reviewId);
}