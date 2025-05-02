package com.incon.backend.controller;

import com.incon.backend.dto.request.CartItemRequest;
import com.incon.backend.dto.response.CartResponse;
import com.incon.backend.entity.Buyer;
import com.incon.backend.entity.Product;
import com.incon.backend.entity.Seller;
import com.incon.backend.integration.BaseIntegrationTest;
import com.incon.backend.repository.BuyerRepository;
import com.incon.backend.repository.ProductRepository;
import com.incon.backend.repository.SellerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CartControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SellerRepository sellerRepository;

    private Buyer testBuyer;
    private Product testProduct;

    @BeforeEach
    public void setUp() {
        // Get test buyer
        testBuyer = buyerRepository.findByUserEmail("buyer@test.com")
                .orElseThrow(() -> new RuntimeException("Test data not initialized properly"));

        // Clear the cart before each test
        restTemplate.delete(buildUrl("/api/carts/buyer/" + testBuyer.getUserId() + "/clear"));

        // Get test product - first product from repository
        List<Product> products = productRepository.findAll();
        assertFalse(products.isEmpty(), "Test data should include products");
        testProduct = products.get(0);
    }

    @Test
    public void testGetCartByBuyerId() {
        // Get the cart
        ResponseEntity<CartResponse> response = restTemplate.getForEntity(
                buildUrl("/api/carts/buyer/" + testBuyer.getUserId()),
                CartResponse.class);

        // Assert the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getCartId());
        assertEquals(testBuyer.getUserId(), response.getBody().getBuyerResponse().getUserId());
        // Cart should be empty after setup
        assertTrue(response.getBody().getCartItems().isEmpty(), "Cart should be empty at test start");
    }

    @Test
    public void testAddItemToCart() {
        // Create cart item request with a fixed quantity
        CartItemRequest request = new CartItemRequest();
        request.setProductId(testProduct.getProductId());
        request.setCartItemQuantity(2); // Set quantity to 2

        // Send the request
        ResponseEntity<CartResponse> response = restTemplate.postForEntity(
                buildUrl("/api/carts/buyer/" + testBuyer.getUserId() + "/items"),
                request,
                CartResponse.class);

        // Assert the response
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getCartId());
        assertEquals(testBuyer.getUserId(), response.getBody().getBuyerResponse().getUserId());

        // Verify cart has exactly one item
        assertEquals(1, response.getBody().getCartItems().size(), "Cart should have exactly one item");

        // Verify cart item details
        var cartItem = response.getBody().getCartItems().get(0);
        assertEquals(testProduct.getProductId(), cartItem.getProductId());
        assertEquals(2, cartItem.getCartItemQuantity(), "Cart item quantity should be 2");
    }


    @Test
    public void testUpdateCartItem() {
        // First add an item to cart
        Buyer buyer = buyerRepository.findByUserEmail("buyer@test.com")
                .orElseThrow(() -> new RuntimeException("Test data not initialized properly"));

        Product product = productRepository.findAll().get(0);

        // Create and add cart item
        CartItemRequest addRequest = new CartItemRequest();
        addRequest.setProductId(product.getProductId());
        addRequest.setCartItemQuantity(1);

        ResponseEntity<CartResponse> addResponse = restTemplate.postForEntity(
                buildUrl("/api/carts/buyer/" + buyer.getUserId() + "/items"),
                addRequest,
                CartResponse.class);

        // Get the cart item ID
        Integer cartItemId = addResponse.getBody().getCartItems().stream()
                .filter(item -> item.getProductId().equals(product.getProductId()))
                .findFirst()
                .map(item -> item.getCartItemId())
                .orElseThrow(() -> new RuntimeException("Failed to add item to cart"));

        // Create update request
        CartItemRequest updateRequest = new CartItemRequest();
        updateRequest.setProductId(product.getProductId());
        updateRequest.setCartItemQuantity(5);

        // Send the update request
        ResponseEntity<CartResponse> updateResponse = restTemplate.exchange(
                buildUrl("/api/carts/buyer/" + buyer.getUserId() + "/items/" + cartItemId),
                HttpMethod.PUT,
                new HttpEntity<>(updateRequest),
                CartResponse.class);

        // Assert the response
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertNotNull(updateResponse.getBody());

        // Verify cart item was updated
        boolean foundUpdatedItem = false;
        for (var item : updateResponse.getBody().getCartItems()) {
            if (item.getProductId().equals(product.getProductId())) {
                foundUpdatedItem = true;
                assertEquals(5, item.getCartItemQuantity());
                break;
            }
        }
        assertTrue(foundUpdatedItem, "Updated product should be in cart with new quantity");
    }

    @Test
    public void testRemoveItemFromCart() {
        // First add an item to cart
        Buyer buyer = buyerRepository.findByUserEmail("buyer@test.com")
                .orElseThrow(() -> new RuntimeException("Test data not initialized properly"));

        Product product = productRepository.findAll().get(0);

        // Create and add cart item
        CartItemRequest addRequest = new CartItemRequest();
        addRequest.setProductId(product.getProductId());
        addRequest.setCartItemQuantity(1);

        restTemplate.postForEntity(
                buildUrl("/api/carts/buyer/" + buyer.getUserId() + "/items"),
                addRequest,
                CartResponse.class);

        // Remove the item
        ResponseEntity<CartResponse> removeResponse = restTemplate.exchange(
                buildUrl("/api/carts/buyer/" + buyer.getUserId() + "/items/" + product.getProductId()),
                HttpMethod.DELETE,
                null,
                CartResponse.class);

        // Assert the response
        assertEquals(HttpStatus.OK, removeResponse.getStatusCode());
        assertNotNull(removeResponse.getBody());

        // Verify cart item was removed
        boolean itemStillExists = removeResponse.getBody().getCartItems().stream()
                .anyMatch(item -> item.getProductId().equals(product.getProductId()));

        assertFalse(itemStillExists, "Item should have been removed from cart");
    }

    @Test
    public void testClearCart() {
        // First add multiple items to cart
        Buyer buyer = buyerRepository.findByUserEmail("buyer@test.com")
                .orElseThrow(() -> new RuntimeException("Test data not initialized properly"));

        List<Product> products = productRepository.findAll();

        // Add 2 products to cart
        for (int i = 0; i < 2 && i < products.size(); i++) {
            CartItemRequest addRequest = new CartItemRequest();
            addRequest.setProductId(products.get(i).getProductId());
            addRequest.setCartItemQuantity(i + 1);

            restTemplate.postForEntity(
                    buildUrl("/api/carts/buyer/" + buyer.getUserId() + "/items"),
                    addRequest,
                    CartResponse.class);
        }

        // Clear the cart
        restTemplate.delete(buildUrl("/api/carts/buyer/" + buyer.getUserId() + "/clear"));

        // Verify cart is empty
        ResponseEntity<CartResponse> getResponse = restTemplate.getForEntity(
                buildUrl("/api/carts/buyer/" + buyer.getUserId()),
                CartResponse.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertTrue(getResponse.getBody().getCartItems().isEmpty(), "Cart should be empty after clear");
    }
}