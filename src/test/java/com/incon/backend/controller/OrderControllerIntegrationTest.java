package com.incon.backend.integration.controller;

import com.incon.backend.dto.request.CartItemRequest;
import com.incon.backend.dto.request.OrderRequest;
import com.incon.backend.dto.response.CartResponse;
import com.incon.backend.dto.response.OrderResponse;
import com.incon.backend.entity.Buyer;
import com.incon.backend.entity.Product;
import com.incon.backend.enums.OrderStatus;
import com.incon.backend.integration.BaseIntegrationTest;
import com.incon.backend.repository.BuyerRepository;
import com.incon.backend.repository.OrderRepository;
import com.incon.backend.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    private Buyer testBuyer;
    private Product testProduct;

    @BeforeEach
    public void setUp() {
        // Ensure we have a clean cart for each test
        Buyer buyer = buyerRepository.findByUserEmail("buyer@test.com")
                .orElseThrow(() -> new RuntimeException("Test data not initialized properly"));

        testBuyer = buyer;

        // Get a product
        List<Product> products = productRepository.findAll();
        assertFalse(products.isEmpty(), "Test data should include products");

        testProduct = products.get(0);

        // Clear the cart
        restTemplate.delete(buildUrl("/api/carts/buyer/" + buyer.getUserId() + "/clear"));
    }

    @Test
    public void testCreateOrder() {
        // Add a product to cart
        CartItemRequest cartRequest = new CartItemRequest();
        cartRequest.setProductId(testProduct.getProductId());
        cartRequest.setCartItemQuantity(2);

        restTemplate.postForEntity(
                buildUrl("/api/carts/buyer/" + testBuyer.getUserId() + "/items"),
                cartRequest,
                CartResponse.class);

        // Create order
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderShippingAddress("123 Test St, Test City, 12345");
        orderRequest.setOrderBillingAddress("123 Test St, Test City, 12345");
        orderRequest.setOrderNotes("Test order notes");

        ResponseEntity<OrderResponse> response = restTemplate.postForEntity(
                buildUrl("/api/orders/buyer/" + testBuyer.getUserId()),
                orderRequest,
                OrderResponse.class);

        // Assert the response
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getOrderId());
        assertEquals(testBuyer.getUserId(), response.getBody().getBuyerId());
        assertEquals(OrderStatus.PENDING, response.getBody().getOrderStatus());
        assertEquals("123 Test St, Test City, 12345", response.getBody().getOrderShippingAddress());

        // Verify order items
        assertEquals(1, response.getBody().getOrderItems().size());
        assertEquals(testProduct.getProductId(), response.getBody().getOrderItems().get(0).getProductId());
        assertEquals(2, response.getBody().getOrderItems().get(0).getOrderItemQuantity());

        // Verify cart is now empty
        ResponseEntity<CartResponse> cartResponse = restTemplate.getForEntity(
                buildUrl("/api/carts/buyer/" + testBuyer.getUserId()),
                CartResponse.class);

        assertTrue(cartResponse.getBody().getCartItems().isEmpty(), "Cart should be empty after order creation");
    }

    @Test
    public void testGetOrderById() {
        // First create an order
        // Add a product to cart
        CartItemRequest cartRequest = new CartItemRequest();
        cartRequest.setProductId(testProduct.getProductId());
        cartRequest.setCartItemQuantity(1);

        restTemplate.postForEntity(
                buildUrl("/api/carts/buyer/" + testBuyer.getUserId() + "/items"),
                cartRequest,
                CartResponse.class);

        // Create order
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderShippingAddress("123 Test St, Test City, 12345");
        orderRequest.setOrderBillingAddress("123 Test St, Test City, 12345");

        ResponseEntity<OrderResponse> createResponse = restTemplate.postForEntity(
                buildUrl("/api/orders/buyer/" + testBuyer.getUserId()),
                orderRequest,
                OrderResponse.class);

        Integer orderId = createResponse.getBody().getOrderId();

        // Get the order by ID
        ResponseEntity<OrderResponse> getResponse = restTemplate.getForEntity(
                buildUrl("/api/orders/" + orderId),
                OrderResponse.class);

        // Assert the response
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals(orderId, getResponse.getBody().getOrderId());
        assertEquals(testBuyer.getUserId(), getResponse.getBody().getBuyerId());
        assertEquals(OrderStatus.PENDING, getResponse.getBody().getOrderStatus());
    }

    @Test
    public void testUpdateOrderStatus() {
        // First create an order
        // Add a product to cart
        CartItemRequest cartRequest = new CartItemRequest();
        cartRequest.setProductId(testProduct.getProductId());
        cartRequest.setCartItemQuantity(1);

        restTemplate.postForEntity(
                buildUrl("/api/carts/buyer/" + testBuyer.getUserId() + "/items"),
                cartRequest,
                CartResponse.class);

        // Create order
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderShippingAddress("123 Test St, Test City, 12345");
        orderRequest.setOrderBillingAddress("123 Test St, Test City, 12345");

        ResponseEntity<OrderResponse> createResponse = restTemplate.postForEntity(
                buildUrl("/api/orders/buyer/" + testBuyer.getUserId()),
                orderRequest,
                OrderResponse.class);

        Integer orderId = createResponse.getBody().getOrderId();

// Update order status to PROCESSING
        ResponseEntity<OrderResponse> updateResponse = restTemplate.exchange(
                buildUrl("/api/orders/" + orderId + "/status?status=PROCESSING"),
                HttpMethod.PATCH,
                null,
                OrderResponse.class);

        // Assert the response
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertNotNull(updateResponse.getBody());
        assertEquals(orderId, updateResponse.getBody().getOrderId());
        assertEquals(OrderStatus.PROCESSING, updateResponse.getBody().getOrderStatus());

        // Verify the status was updated in the database
        OrderResponse getResponse = restTemplate.getForEntity(
                buildUrl("/api/orders/" + orderId),
                OrderResponse.class).getBody();

        assertEquals(OrderStatus.PROCESSING, getResponse.getOrderStatus());
    }

    @Test
    public void testCancelOrder() {
        // First create an order
        // Add a product to cart
        CartItemRequest cartRequest = new CartItemRequest();
        cartRequest.setProductId(testProduct.getProductId());
        cartRequest.setCartItemQuantity(1);

        restTemplate.postForEntity(
                buildUrl("/api/carts/buyer/" + testBuyer.getUserId() + "/items"),
                cartRequest,
                CartResponse.class);

        // Create order
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderShippingAddress("123 Test St, Test City, 12345");
        orderRequest.setOrderBillingAddress("123 Test St, Test City, 12345");

        ResponseEntity<OrderResponse> createResponse = restTemplate.postForEntity(
                buildUrl("/api/orders/buyer/" + testBuyer.getUserId()),
                orderRequest,
                OrderResponse.class);

        Integer orderId = createResponse.getBody().getOrderId();

        // Cancel the order
        ResponseEntity<OrderResponse> cancelResponse = restTemplate.exchange(
                buildUrl("/api/orders/" + orderId + "/cancel"),
                HttpMethod.PATCH,
                null,
                OrderResponse.class);

        // Assert the response
        assertEquals(HttpStatus.OK, cancelResponse.getStatusCode());
        assertNotNull(cancelResponse.getBody());
        assertEquals(orderId, cancelResponse.getBody().getOrderId());
        assertEquals(OrderStatus.CANCELLED, cancelResponse.getBody().getOrderStatus());

        // Verify the status was updated in the database
        OrderResponse getResponse = restTemplate.getForEntity(
                buildUrl("/api/orders/" + orderId),
                OrderResponse.class).getBody();

        assertEquals(OrderStatus.CANCELLED, getResponse.getOrderStatus());
    }
}