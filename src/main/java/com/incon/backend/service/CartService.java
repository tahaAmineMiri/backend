package com.incon.backend.service;

import com.incon.backend.dto.request.CartItemRequest;
import com.incon.backend.dto.response.CartResponse;

public interface CartService {
    CartResponse getCartByUserId(Integer userId);
    CartResponse addItemToCart(Integer userId, CartItemRequest request);
    CartResponse updateCartItem(Integer userId, Integer cartItemId, CartItemRequest request);
    CartResponse removeItemFromCart(Integer userId, Integer productId);
    void clearCart(Integer userId);
}