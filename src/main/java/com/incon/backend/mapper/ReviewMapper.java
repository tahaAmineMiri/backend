package com.incon.backend.mapper;

import com.incon.backend.dto.request.ReviewRequest;
import com.incon.backend.dto.response.ReviewResponse;
import com.incon.backend.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "reviewId", ignore = true)
    @Mapping(target = "reviewDate", ignore = true)
    @Mapping(target = "reviewUser", ignore = true)
    @Mapping(target = "reviewProduct", ignore = true)
    @Mapping(target = "reviewCreatedAt", ignore = true)
    @Mapping(target = "reviewUpdatedAt", ignore = true)
    Review toReview(ReviewRequest reviewRequest);

    @Mapping(source = "reviewUser.userId", target = "userId")
    @Mapping(source = "reviewUser.userFullName", target = "userFullName")
    @Mapping(source = "reviewProduct.productId", target = "productId")
    @Mapping(source = "reviewProduct.productName", target = "productName")
    ReviewResponse toReviewResponse(Review review);

    @Mapping(target = "reviewId", ignore = true)
    @Mapping(target = "reviewDate", ignore = true)
    @Mapping(target = "reviewUser", ignore = true)
    @Mapping(target = "reviewProduct", ignore = true)
    @Mapping(target = "reviewCreatedAt", ignore = true)
    @Mapping(target = "reviewUpdatedAt", ignore = true)
    void updateReviewFromRequest(ReviewRequest reviewRequest, @MappingTarget Review review);
}