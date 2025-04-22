package com.incon.backend.mapper;

import com.incon.backend.dto.response.OrderItemResponse;
import com.incon.backend.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    @Mapping(source = "product.id", target = "productId")
    OrderItemResponse toResponse(OrderItem orderItem);
}