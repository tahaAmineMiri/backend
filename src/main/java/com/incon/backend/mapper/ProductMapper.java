//package com.incon.backend.mapper;
//
//import com.incon.backend.dto.response.ProductResponse;
//import com.incon.backend.entity.Product;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//
//@Mapper(componentModel = "spring")
//public interface ProductMapper {
//
//    @Mapping(target = "sellerId", source = "seller.userId")
//    @Mapping(target = "sellerName", source = "seller.fullName")
//    ProductResponse toProductResponse(Product product);
//}