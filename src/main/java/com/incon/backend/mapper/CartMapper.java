package com.incon.backend.mapper;

import com.incon.backend.dto.response.CartItemResponse;
import com.incon.backend.dto.response.CartResponse;
import com.incon.backend.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = CartItemMapper.class)
public interface CartMapper {

    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

    @Mapping(source = "cartId", target = "cartId")
    @Mapping(source = "totalAmount", target = "totalAmount")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    @Mapping(source = "buyer.userId", target = "buyerId")
    @Mapping(source = "cartItems", target = "items")
    CartResponse toResponse(Cart cart);
}
