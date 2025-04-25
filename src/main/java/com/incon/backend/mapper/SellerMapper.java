package com.incon.backend.mapper;

import com.incon.backend.dto.request.SellerRequest;
import com.incon.backend.dto.response.SellerResponse;
import com.incon.backend.entity.Seller;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SellerMapper {
    // Convert Seller entity to SellerResponse DTO
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "userEmail", target = "userEmail")
    @Mapping(source = "userFullName", target = "userFullName")
    @Mapping(source = "userPosition", target = "userPosition")
    @Mapping(source = "userBusinessPhone", target = "userBusinessPhone")
    @Mapping(source = "userIsVerified", target = "userIsVerified")
    SellerResponse toSellerResponse(Seller seller);

    // Convert SellerRequest DTO to Seller entity
    @Mapping(source = "userEmail", target = "userEmail")
    @Mapping(source = "userPassword", target = "userPassword")
    @Mapping(source = "userFullName", target = "userFullName")
    @Mapping(source = "userPosition", target = "userPosition")
    @Mapping(source = "userBusinessPhone", target = "userBusinessPhone")
    @Mapping(source = "userRole", target = "userRole")
    Seller toSeller(SellerRequest request);
}