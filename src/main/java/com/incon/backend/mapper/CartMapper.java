package com.incon.backend.mapper;

import com.incon.backend.dto.response.CartResponse;
import com.incon.backend.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = CartItemMapper.class)
public interface CartMapper {
    @Mapping(source = "cartId", target = "cartId")
    @Mapping(source = "cartTotalAmount", target = "cartTotalAmount")
    @Mapping(source = "cartBuyer", target = "buyerResponse")
    @Mapping(source = "cartItems", target = "cartItems")
    CartResponse toCartResponse(Cart cart);
}