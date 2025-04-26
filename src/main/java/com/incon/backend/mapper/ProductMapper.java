package com.incon.backend.mapper;

import com.incon.backend.dto.request.ProductRequest;
import com.incon.backend.dto.response.ProductResponse;
import com.incon.backend.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "productId", target = "productId")
    @Mapping(source = "productName", target = "productName")
    @Mapping(source = "productDescription", target = "productDescription")
    @Mapping(source = "productPrice", target = "productPrice")
    @Mapping(source = "productStockQuantity", target = "productStockQuantity")
    @Mapping(source = "productCategory", target = "productCategory")
    @Mapping(source = "productImage", target = "productImage")
    @Mapping(source = "productRating", target = "productRating")
    ProductResponse toProductResponse(Product product);

    List<ProductResponse> toProductResponseList(List<Product> products);

    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "productSeller", ignore = true)
    @Mapping(target = "productRating", ignore = true)
    @Mapping(source = "productName", target = "productName")
    @Mapping(source = "productDescription", target = "productDescription")
    @Mapping(source = "productPrice", target = "productPrice")
    @Mapping(source = "productStockQuantity", target = "productStockQuantity")
    @Mapping(source = "productCategory", target = "productCategory")
    @Mapping(source = "productImage", target = "productImage")
    Product toProduct(ProductRequest productRequest);

    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "productSeller", ignore = true)
    @Mapping(target = "productRating", ignore = true)
    @Mapping(source = "productName", target = "productName")
    @Mapping(source = "productDescription", target = "productDescription")
    @Mapping(source = "productPrice", target = "productPrice")
    @Mapping(source = "productStockQuantity", target = "productStockQuantity")
    @Mapping(source = "productCategory", target = "productCategory")
    @Mapping(source = "productImage", target = "productImage")
    void updateProductFromRequest(ProductRequest productRequest, @MappingTarget Product product);
}