package com.incon.backend.controller;

import com.incon.backend.dto.request.ReviewRequest;
import com.incon.backend.dto.response.ApiResponse;
import com.incon.backend.dto.response.ReviewResponse;
import com.incon.backend.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/user/{userId}")
    public ResponseEntity<ReviewResponse> createReview(
            @PathVariable Integer userId,
            @Valid @RequestBody ReviewRequest reviewRequest) {
        ReviewResponse response = reviewService.createReview(userId, reviewRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> getReviewById(@PathVariable Integer reviewId) {
        ReviewResponse response = reviewService.getReviewById(reviewId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByUserId(@PathVariable Integer userId) {
        List<ReviewResponse> responses = reviewService.getReviewsByUserId(userId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByProductId(@PathVariable Integer productId) {
        List<ReviewResponse> responses = reviewService.getReviewsByProductId(productId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/rating/{rating}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByRating(@PathVariable Integer rating) {
        List<ReviewResponse> responses = reviewService.getReviewsByRating(rating);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<ReviewResponse>> getReviewsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<ReviewResponse> responses = reviewService.getReviewsByDateRange(startDate, endDate);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{reviewId}/user/{userId}")
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable Integer reviewId,
            @PathVariable Integer userId,
            @Valid @RequestBody ReviewRequest reviewRequest) {
        ReviewResponse response = reviewService.updateReview(reviewId, userId, reviewRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{reviewId}/user/{userId}")
    public ResponseEntity<ApiResponse> deleteReview(
            @PathVariable Integer reviewId,
            @PathVariable Integer userId) {
        reviewService.deleteReview(reviewId, userId);
        return ResponseEntity.ok(new ApiResponse("Review deleted successfully", HttpStatus.OK));
    }

    @GetMapping("/user/{userId}/product/{productId}/check")
    public ResponseEntity<ApiResponse> checkUserReviewedProduct(
            @PathVariable Integer userId,
            @PathVariable Integer productId) {
        boolean hasReviewed = reviewService.hasUserReviewedProduct(userId, productId);

        if (hasReviewed) {
            return ResponseEntity.ok(new ApiResponse("User has already reviewed this product", HttpStatus.OK));
        } else {
            return ResponseEntity.ok(new ApiResponse("User has not reviewed this product yet", HttpStatus.OK));
        }
    }
}