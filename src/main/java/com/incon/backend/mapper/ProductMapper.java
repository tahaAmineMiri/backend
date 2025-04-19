package com.incon.backend.mapper;

import com.incon.backend.dto.response.ProductResponse;
import com.incon.backend.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponse toProductResponse(Product product);
}