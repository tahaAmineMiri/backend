//package com.incon.backend.mapper;
//
//import com.incon.backend.dto.request.SellerRegistrationRequest;
//import com.incon.backend.dto.response.SellerResponse;
//import com.incon.backend.entity.Product;
//import com.incon.backend.entity.Seller;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Mapper(componentModel = "spring", uses = UserMapper.class)
//public interface SellerMapper {
//
//    Seller toEntity(SellerRegistrationRequest request);
//
//    @Mapping(target = "productIds", source = "products", qualifiedByName = "mapProductIds")
//    SellerResponse toResponse(Seller seller);
//
//    default List<Integer> mapProductIds(List<Product> products) {
//        return products != null ? products.stream()
//                .map(Product::getProductId)
//                .collect(Collectors.toList()) : null;
//    }
//}