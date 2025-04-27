package com.incon.backend.controller;

import com.incon.backend.dto.request.CartItemRequest;
import com.incon.backend.dto.response.ApiResponse;
import com.incon.backend.dto.response.CartResponse;
import com.incon.backend.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<CartResponse> getCartByBuyerId(@PathVariable Integer buyerId) {
        CartResponse cartResponse = cartService.getCartByBuyerId(buyerId);
        return ResponseEntity.ok(cartResponse);
    }

    @PostMapping("/buyer/{buyerId}/items")
    public ResponseEntity<CartResponse> addItemToCart(
            @PathVariable Integer buyerId,
            @Valid @RequestBody CartItemRequest request) {
        CartResponse cartResponse = cartService.addItemToCart(buyerId, request);
        return new ResponseEntity<>(cartResponse, HttpStatus.CREATED);
    }

    @PutMapping("/buyer/{buyerId}/items/{cartItemId}")
    public ResponseEntity<CartResponse> updateCartItem(
            @PathVariable Integer buyerId,
            @PathVariable Integer cartItemId,
            @Valid @RequestBody CartItemRequest request) {
        CartResponse cartResponse = cartService.updateCartItem(buyerId, cartItemId, request);
        return ResponseEntity.ok(cartResponse);
    }

    @DeleteMapping("/buyer/{buyerId}/items/{productId}")
    public ResponseEntity<CartResponse> removeItemFromCart(
            @PathVariable Integer buyerId,
            @PathVariable Integer productId) {
        CartResponse cartResponse = cartService.removeItemFromCart(buyerId, productId);
        return ResponseEntity.ok(cartResponse);
    }

    @DeleteMapping("/buyer/{buyerId}/clear")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable Integer buyerId) {
        cartService.clearCart(buyerId);
        return ResponseEntity.ok(new ApiResponse("Cart cleared successfully", HttpStatus.OK));
    }
}