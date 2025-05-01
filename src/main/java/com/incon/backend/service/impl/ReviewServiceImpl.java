package com.incon.backend.service.impl;

import com.incon.backend.dto.request.ReviewRequest;
import com.incon.backend.dto.response.ReviewResponse;
import com.incon.backend.entity.Product;
import com.incon.backend.entity.Review;
import com.incon.backend.entity.User;
import com.incon.backend.exception.BadRequestException;
import com.incon.backend.exception.ResourceNotFoundException;
import com.incon.backend.mapper.ReviewMapper;
import com.incon.backend.repository.ProductRepository;
import com.incon.backend.repository.ReviewRepository;
import com.incon.backend.repository.UserRepository;
import com.incon.backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ReviewMapper reviewMapper;

    @Override
    @Transactional
    public ReviewResponse createReview(Integer userId, ReviewRequest reviewRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Product product = productRepository.findById(reviewRequest.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + reviewRequest.getProductId()));

        // Check if the user has already reviewed this product
        if (hasUserReviewedProduct(userId, reviewRequest.getProductId())) {
            throw new BadRequestException("User has already reviewed this product");
        }

        // Create review
        Review review = reviewMapper.toReview(reviewRequest);
        review.setReviewUser(user);
        review.setReviewProduct(product);
        review.setReviewDate(LocalDateTime.now());

        Review savedReview = reviewRepository.save(review);

        // Update user and product references
        user.addReview(savedReview);
        product.addReview(savedReview);

        userRepository.save(user);
        productRepository.save(product);

        return reviewMapper.toReviewResponse(savedReview);
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewResponse getReviewById(Integer reviewId) {
        Review review = getReviewEntityById(reviewId);
        return reviewMapper.toReviewResponse(review);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByUserId(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        List<Review> reviews = reviewRepository.findByReviewUser_UserId(userId);
        return reviews.stream()
                .map(reviewMapper::toReviewResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByProductId(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        List<Review> reviews = reviewRepository.findByReviewProduct_ProductId(productId);
        return reviews.stream()
                .map(reviewMapper::toReviewResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByRating(Integer rating) {
        List<Review> reviews = reviewRepository.findByReviewRating(rating);
        return reviews.stream()
                .map(reviewMapper::toReviewResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Review> reviews = reviewRepository.findByReviewDateBetween(startDate, endDate);
        return reviews.stream()
                .map(reviewMapper::toReviewResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReviewResponse updateReview(Integer reviewId, Integer userId, ReviewRequest reviewRequest) {
        Review review = getReviewEntityById(reviewId);

        // Verify that the review belongs to the user
        if (!review.getReviewUser().getUserId().equals(userId)) {
            throw new BadRequestException("User is not authorized to update this review");
        }

        // Check if the product ID is being changed
        if (!review.getReviewProduct().getProductId().equals(reviewRequest.getProductId())) {
            throw new BadRequestException("Cannot change the product for an existing review");
        }

        // Store old rating to check if it changed
        Integer oldRating = review.getReviewRating();

        // Update review fields
        reviewMapper.updateReviewFromRequest(reviewRequest, review);
        review.setReviewDate(LocalDateTime.now()); // Update review date to current time

        Review updatedReview = reviewRepository.save(review);

        // If rating changed, update product rating
        if (!oldRating.equals(review.getReviewRating())) {
            Product product = review.getReviewProduct();
            product.updateRating();
            productRepository.save(product);
        }

        return reviewMapper.toReviewResponse(updatedReview);
    }

    @Override
    @Transactional
    public void deleteReview(Integer reviewId, Integer userId) {
        Review review = getReviewEntityById(reviewId);

        // Verify that the review belongs to the user
        if (!review.getReviewUser().getUserId().equals(userId)) {
            throw new BadRequestException("User is not authorized to delete this review");
        }

        // Save product reference before deleting
        Product product = review.getReviewProduct();

        // Remove references from user and product
        review.getReviewUser().removeReview(review);
        product.removeReview(review);

        // Delete review
        reviewRepository.delete(review);

        // Update product rating
        productRepository.save(product);
    }

    @Override
    public boolean hasUserReviewedProduct(Integer userId, Integer productId) {
        return !reviewRepository.findByReviewProduct_ProductIdAndReviewUser_UserId(productId, userId).isEmpty();
    }

    @Override
    @Transactional(readOnly = true)
    public Review getReviewEntityById(Integer reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + reviewId));
    }
}