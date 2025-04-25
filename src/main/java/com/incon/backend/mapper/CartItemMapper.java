package com.incon.backend.mapper;

import com.incon.backend.dto.response.CartItemResponse;
import com.incon.backend.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    @Mapping(source = "cartItemId", target = "cartItemId")
    @Mapping(source = "cartItemQuantity", target = "cartItemQuantity")
    @Mapping(source = "cartItemPrice", target = "cartItemPrice")
    @Mapping(source = "cartItemSubtotal", target = "cartItemSubtotal")
    @Mapping(source = "cartItemProduct.productId", target = "productId")
    @Mapping(source = "cartItemProduct.productName", target = "productName")
    @Mapping(source = "cartItemProduct.productImage", target = "productImage")
    CartItemResponse toResponse(CartItem cartItem);
}