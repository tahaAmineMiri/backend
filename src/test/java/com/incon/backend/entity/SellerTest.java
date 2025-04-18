package com.incon.backend.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SellerTest {

    private Seller seller;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        seller = new Seller();
        product1 = new Product();
        product2 = new Product();
    }

    @Test
    void testAddProduct() {
        seller.addProduct(product1);

        List<Product> products = seller.getProducts();
        assertEquals(1, products.size());
        assertTrue(products.contains(product1));
    }

    @Test
    void testInitialProductsListIsEmpty() {
        assertNotNull(seller.getProducts());
        assertTrue(seller.getProducts().isEmpty());
    }

    @Test
    void testRemoveProduct() {
        seller.addProduct(product1);
        seller.addProduct(product2);

        seller.removeProduct(product1);

        List<Product> products = seller.getProducts();
        assertEquals(1, products.size());
    }
}