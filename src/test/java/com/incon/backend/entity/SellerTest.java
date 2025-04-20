//package com.incon.backend.entity;
//
//import com.incon.backend.enums.Role;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class SellerTest {
//
//    private Seller seller;
//    private Product product1;
//    private Product product2;
//
//    @BeforeEach
//    void setUp() {
//        // Initialize seller
//        seller = new Seller();
//
//        // Set User properties (from parent class)
//        seller.setId(1);
//        seller.setEmail("seller@example.com");
//        seller.setPassword("securePassword123");
//        seller.setRole(Role.SELLER);
//        seller.setFullName("John Seller");
//        seller.setPosition("Store Owner");
//        seller.setBusinessPhone("123-456-7890");
//        seller.setVerified(true);
//
//        // Initialize test products
//        product1 = new Product();
//        product1.setProductId(101);
//        product1.setName("Test Product 1");
//        product1.setPrice(99.99f);
//
//        product2 = new Product();
//        product2.setProductId(102);
//        product2.setName("Test Product 2");
//        product2.setPrice(149.99f);
//    }
//
//    @Test
//    void testSellerCreation() {
//        assertNotNull(seller, "Seller should be created successfully");
//        assertEquals(1, seller.getId(), "Seller ID should match the set value");
//        assertEquals("seller@example.com", seller.getEmail(), "Email should match the set value");
//        assertEquals("securePassword123", seller.getPassword(), "Password should match the set value");
//        assertEquals(Role.SELLER, seller.getRole(), "Role should be SELLER");
//        assertEquals("John Seller", seller.getFullName(), "Full name should match the set value");
//        assertEquals("Store Owner", seller.getPosition(), "Position should match the set value");
//        assertEquals("123-456-7890", seller.getBusinessPhone(), "Business phone should match the set value");
//        assertTrue(seller.isVerified(), "Seller should be verified");
//    }
//
//    @Test
//    void testSellerInheritanceFromUser() {
//        // Verify that Seller inherits properties from User
//        User user = seller;
//        assertNotNull(user, "Seller should be castable to User");
//        assertEquals("seller@example.com", user.getEmail(), "User's email should match seller's email");
//        assertEquals("John Seller", user.getFullName(), "User's full name should match seller's full name");
//        assertEquals(Role.SELLER, user.getRole(), "User's role should be SELLER");
//    }
//
//    @Test
//    void testProductsList() {
//        // Initially, product list should be empty
//        assertTrue(seller.getProducts().isEmpty(), "Products list should be empty initially");
//
//        // Products list should be initialized (not null)
//        assertNotNull(seller.getProducts(), "Products list should be initialized");
//    }
//
//    @Test
//    void testAddProduct() {
//        // Add products
//        seller.addProduct(product1);
//
//        // Verify products were added
//        assertEquals(1, seller.getProducts().size(), "Products list should contain 1 product");
//        assertTrue(seller.getProducts().contains(product1), "Products list should contain product1");
//
//        // Add another product
//        seller.addProduct(product2);
//
//        // Verify second product was added
//        assertEquals(2, seller.getProducts().size(), "Products list should contain 2 products");
//        assertTrue(seller.getProducts().contains(product2), "Products list should contain product2");
//    }
//
//    @Test
//    void testRemoveProduct() {
//        // Add products
//        seller.addProduct(product1);
//        seller.addProduct(product2);
//
//        // Verify both products are added
//        assertEquals(2, seller.getProducts().size(), "Products list should contain 2 products initially");
//
//        // Remove one product
//        seller.removeProduct(product1);
//
//        // Verify product was removed
//        assertEquals(1, seller.getProducts().size(), "Products list should contain 1 product after removal");
//        assertFalse(seller.getProducts().contains(product1), "Products list should not contain product1 after removal");
//        assertTrue(seller.getProducts().contains(product2), "Products list should still contain product2");
//
//        // Remove another product
//        seller.removeProduct(product2);
//
//        // Verify product was removed
//        assertTrue(seller.getProducts().isEmpty(), "Products list should be empty after removing all products");
//    }
//
//    @Test
//    void testAllArgsConstructor() {
//        // Create a list of products
//        List<Product> productList = List.of(product1, product2);
//
//        // Create seller using all-args constructor
//        Seller newSeller = new Seller(productList);
//
//        // Set other required fields
//        newSeller.setId(2);
//        newSeller.setEmail("seller2@example.com");
//        newSeller.setPassword("password456");
//        newSeller.setRole(Role.SELLER);
//        newSeller.setFullName("Jane Seller");
//
//        // Verify all properties
//        assertEquals(2, newSeller.getId(), "Seller ID should match");
//        assertEquals("seller2@example.com", newSeller.getEmail(), "Email should match");
//        assertEquals("Jane Seller", newSeller.getFullName(), "Full name should match");
//        assertEquals(2, newSeller.getProducts().size(), "Products list size should match");
//        assertTrue(newSeller.getProducts().contains(product1), "Products list should contain product1");
//        assertTrue(newSeller.getProducts().contains(product2), "Products list should contain product2");
//    }
//
//    @Test
//    void testTimestamps() {
//        // Create a new seller
//        Seller newSeller = new Seller();
//
//        // Initially timestamps should be null (as they are set by Hibernate)
//        assertNull(newSeller.getCreatedAt(), "CreatedAt should be null initially");
//        assertNull(newSeller.getUpdatedAt(), "UpdatedAt should be null initially");
//
//        // Simulate timestamps being set (normally done by Hibernate)
//        LocalDateTime now = LocalDateTime.now();
//        // Requires setter methods in User class or reflection to test
//        // This is just to show what would be tested if setters were available:
//
//        // If setters are not available, you can just verify the structure and annotation
//        // existence in another type of test, not unit test
//    }
//
//    @Test
//    void testNoArgsConstructor() {
//        // Test the no-args constructor
//        Seller newSeller = new Seller();
//
//        // Verify initialization
//        assertNotNull(newSeller, "Seller should be created with no-args constructor");
//        assertNotNull(newSeller.getProducts(), "Products list should be initialized");
//        assertTrue(newSeller.getProducts().isEmpty(), "Products list should be empty");
//    }
//}