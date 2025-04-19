package com.incon.backend.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        // Initialize the Product object before each test, passing the mock Seller
        product1 = new Product();
        product2 = new Product("Test Product", "Test Description", 100.0f, 10, "Test Category", "test_image.jpg");
    }

    @Test
    void testProductCreation() {
        assertNotNull(product1);
        assertNotNull(product2);
    }

    @Test
    void testUpdateStock() {
        product2.updateStock(20);
        assertEquals(20, product2.getStockQuantity());
    }

    @Test
    void testUpdatePrice() {
        product2.updatePrice(120.0f);
        assertEquals(120.0f, product2.getPrice());
    }
}
