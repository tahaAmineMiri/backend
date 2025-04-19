package com.incon.backend.mapper;

import com.incon.backend.dto.request.SellerRequest;
import com.incon.backend.dto.response.SellerResponse;
import com.incon.backend.entity.Seller;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SellerMapper {
    // Convert Seller entity to SellerResponse DTO
    SellerResponse toSellerResponse(Seller seller);

    // Convert SellerRequest DTO to Seller entity
    Seller toEntity(SellerRequest request);
}