package com.incon.backend.mapper;

import com.incon.backend.dto.request.ProductRequest;
import com.incon.backend.dto.response.ProductResponse;
import com.incon.backend.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    // Single entity to response DTO
    ProductResponse toProductResponse(Product product);

    // List of entities to list of response DTOs
    List<ProductResponse> toProductResponseList(List<Product> products);

    // Request DTO to entity (for saving or updating)
    Product toProduct(ProductRequest productRequest);

    // Method to update an existing product entity with values from the product request
    void updateProductFromRequest(ProductRequest productRequest, @MappingTarget Product product);
}