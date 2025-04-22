package com.incon.backend.mapper;

import com.incon.backend.dto.response.CartItemResponse;
import com.incon.backend.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    CartItemMapper INSTANCE = Mappers.getMapper(CartItemMapper.class);

    @Mapping(source = "cartItemId", target = "cartItemId")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "itemPrice", target = "itemPrice")
    @Mapping(source = "subtotal", target = "subtotal")
    @Mapping(source = "product.productId", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.image", target = "productImage")
    CartItemResponse toResponse(CartItem cartItem);
}
