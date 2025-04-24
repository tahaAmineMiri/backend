package com.incon.backend.service;

import com.incon.backend.dto.request.CartItemRequest;
import com.incon.backend.dto.response.CartResponse;

public interface CartService {
    CartResponse getCartByBuyerId(Integer userId);
    CartResponse addItemToCart(Integer buyerId, CartItemRequest request);
    CartResponse updateCartItem(Integer buyerId, Integer cartItemId, CartItemRequest request);
    CartResponse removeItemFromCart(Integer buyerId, Integer productId);
    void clearCart(Integer buyerId);
}