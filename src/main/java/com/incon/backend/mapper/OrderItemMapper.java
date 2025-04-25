package com.incon.backend.mapper;

import com.incon.backend.dto.response.OrderItemResponse;
import com.incon.backend.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    @Mapping(source = "orderItemId", target = "orderItemId")
    @Mapping(source = "orderItemQuantity", target = "orderItemQuantity")
    @Mapping(source = "orderItemPrice", target = "orderItemPrice")
    @Mapping(source = "orderItemSubtotal", target = "orderItemSubtotal")
    @Mapping(source = "orderItemProduct.productId", target = "productId")
    @Mapping(source = "orderItemProduct.productName", target = "productName")
    @Mapping(source = "orderItemProduct.productImage", target = "productImage")
    OrderItemResponse toResponse(OrderItem orderItem);
}