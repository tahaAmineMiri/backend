package com.incon.backend.controller;

import com.incon.backend.dto.request.ProductRequest;
import com.incon.backend.dto.response.ProductResponse;
import com.incon.backend.entity.Product;
import com.incon.backend.entity.Seller;
import com.incon.backend.integration.BaseIntegrationTest;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ProductControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SellerRepository sellerRepository;

    private Seller testSeller;
    private Product testProductForDelete;

    @BeforeEach
    public void setUp() {
        // Get a seller for testing
        testSeller = sellerRepository.findByUserEmail("seller@test.com")
                .orElseThrow(() -> new RuntimeException("Test data not initialized properly"));

        // Create a product specifically for the delete test
        String uniqueName = "Product To Delete " + UUID.randomUUID().toString();

        Product deleteTestProduct = new Product();
        deleteTestProduct.setProductName(uniqueName);
        deleteTestProduct.setProductDescription("This product will be deleted");
        deleteTestProduct.setProductPrice(new BigDecimal("99.99"));
        deleteTestProduct.setProductStockQuantity(10);
        deleteTestProduct.setProductCategory("Test");
        deleteTestProduct.setProductImage("http://example.com/test.jpg");
        deleteTestProduct.setProductSeller(testSeller);
        deleteTestProduct.setProductRating(0.0f);
        deleteTestProduct.setProductReviews(new ArrayList<>());

        testProductForDelete = productRepository.save(deleteTestProduct);
    }

    @Test
    public void testCreateProduct() {
        // Create a product request
        ProductRequest request = new ProductRequest();
        request.setProductName("New Test Product");
        request.setProductDescription("This is a new test product");
        request.setProductPrice(new BigDecimal("129.99"));
        request.setProductStockQuantity(100);
        request.setProductCategory("Technology");
        request.setProductImage("http://example.com/newproduct.jpg");

        // Send the request
        ResponseEntity<ProductResponse> response = restTemplate.postForEntity(
                buildUrl("/api/products/seller/" + testSeller.getUserId()),
                request,
                ProductResponse.class);

        // Assert the response
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getProductId());
        assertEquals("New Test Product", response.getBody().getProductName());
        assertEquals("This is a new test product", response.getBody().getProductDescription());
        assertEquals(0, new BigDecimal("129.99").compareTo(response.getBody().getProductPrice()));
        assertEquals(100, response.getBody().getProductStockQuantity());
        assertEquals("Technology", response.getBody().getProductCategory());

        // Verify the product was persisted
        assertTrue(productRepository.existsById(response.getBody().getProductId()));
    }

    @Test
    public void testGetAllProducts() {
        // Get all products
        ResponseEntity<ProductResponse[]> response = restTemplate.getForEntity(
                buildUrl("/api/products"),
                ProductResponse[].class);

        // Assert the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length >= 3, "Should have at least 3 products from test data initialization");
    }

    @Test
    public void testGetProductById() {
        // Get a product from the repository
        List<Product> products = productRepository.findAll();
        assertFalse(products.isEmpty(), "Test data should include products");

        Product product = products.get(0);

        // Get the product by ID
        ResponseEntity<ProductResponse> response = restTemplate.getForEntity(
                buildUrl("/api/products/" + product.getProductId()),
                ProductResponse.class);

        // Assert the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(product.getProductId(), response.getBody().getProductId());
        assertEquals(product.getProductName(), response.getBody().getProductName());
    }

    @Test
    public void testUpdateProduct() {
        // Get a product from the repository
        List<Product> products = productRepository.findAll();
        assertFalse(products.isEmpty(), "Test data should include products");

        Product product = products.get(0);

        // Create update request
        ProductRequest request = new ProductRequest();
        request.setProductName("Updated Product Name");
        request.setProductDescription("Updated description");
        request.setProductPrice(new BigDecimal("199.99"));
        request.setProductStockQuantity(200);
        request.setProductCategory(product.getProductCategory()); // Keep the same category
        request.setProductImage(product.getProductImage()); // Keep the same image

        // Send the update request
        ResponseEntity<ProductResponse> response = restTemplate.exchange(
                buildUrl("/api/products/" + product.getProductId()),
                HttpMethod.PUT,
                new HttpEntity<>(request),
                ProductResponse.class);

        // Assert the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(product.getProductId(), response.getBody().getProductId());
        assertEquals("Updated Product Name", response.getBody().getProductName());
        assertEquals("Updated description", response.getBody().getProductDescription());
        assertEquals(0, new BigDecimal("199.99").compareTo(response.getBody().getProductPrice()));
        assertEquals(200, response.getBody().getProductStockQuantity());

        // Verify the update was persisted
        Product updatedProduct = productRepository.findById(product.getProductId()).orElse(null);
        assertNotNull(updatedProduct);
        assertEquals("Updated Product Name", updatedProduct.getProductName());
    }

    @Test
    public void testDeleteProduct() {
        // Use the product created in setUp() specifically for this test
        Integer productIdToDelete = testProductForDelete.getProductId();

        // Verify product exists before deletion
        assertTrue(productRepository.existsById(productIdToDelete),
                "Test product should exist before deletion test");

        // Send delete request
        restTemplate.delete(buildUrl("/api/products/" + productIdToDelete));

        // Verify the product was deleted
        assertFalse(productRepository.existsById(productIdToDelete),
                "Product should not exist after deletion");
    }

}