package com.incon.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private Integer reviewId;
    private Integer reviewRating;
    private String reviewComment;
    private LocalDateTime reviewDate;
    private Integer userId;
    private String userFullName;
    private Integer productId;
    private String productName;
    private LocalDateTime reviewCreatedAt;
}