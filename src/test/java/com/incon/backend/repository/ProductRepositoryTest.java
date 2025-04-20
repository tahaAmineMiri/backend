//package com.incon.backend.repository;
//
//import com.incon.backend.entity.Product;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.domain.EntityScan;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.ContextConfiguration;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//
//// Create a minimal test application config
//@SpringBootApplication(scanBasePackages = "com.incon.backend.repository")
//@EnableJpaRepositories(basePackages = "com.incon.backend.repository")
//@EntityScan(basePackages = "com.incon.backend.entity") // Adjust package if needed
//class TestApplication {
//}
//
//@DataJpaTest
//@ActiveProfiles("test")
//@ContextConfiguration(classes = TestApplication.class) // Use test config instead of main app
//public class ProductRepositoryTest {
//
//    @Autowired
//    private ProductRepository productRepository;
//    Product product1;
//    Product product2;
//    Product product3;
//    Product product4;
//
//    @BeforeEach
//    public void setUp() {
//        product1 = new Product("Test Product", "Test Description", 100.0f, 10, "Test Category", "test_image.jpg");
//        product2 = new Product("Test Product 2", "Test Description 2", 200.0f, 20, "Test Category", "test_image_2.jpg");
//        // Add Other types of products
//        product3 = new Product("Test Product 3", "Test Description 3", 300.0f, 30, "Test Category 3", "test_image_3.jpg");
//        product4 = new Product("Test Product 4", "Test Description 4", 400.0f, 40, "Test Category 4", "test_image_4.jpg");
//
//
//    }
//
//    @Test
//    public void testSaveProduct() {
//        // Your test implementation
//        Product productRepository1 = productRepository.save(product1);
//        Product productRepository2 = productRepository.save(product2);
//
//        // Verify the product is saved correctly
//        assertNotNull(productRepository1);
//        assertNotNull(productRepository2);
//    }
//
//    @Test
//    public void testFindByCategory() {
//        productRepository.save(product1);
//        productRepository.save(product2);
//        productRepository.save(product3);
//        productRepository.save(product4);
//
//        List<Product> products = productRepository.findByCategory("Test Category");
//
//        assertNotNull(products);
//        assertEquals(2, products.size());
//        assertEquals("Test Product", products.get(0).getName());
//    }
//
//    @Test
//    public void testFindByStockQuantityLessThan() {
//        productRepository.save(product1);
//        productRepository.save(product2);
//        productRepository.save(product3);
//        productRepository.save(product4);
//
//        List<Product> products = productRepository.findByStockQuantityLessThan(15);
//
//        assertNotNull(products);
//        assertEquals(1, products.size());
//        assertEquals("Test Product", products.get(0).getName());
//    }
//
//    @Test
//    public void testFindByPriceBetween() {
//        productRepository.save(product1);
//        productRepository.save(product2);
//        productRepository.save(product3);
//        productRepository.save(product4);
//
//        List<Product> products = productRepository.findByPriceBetween(50.0f, 150.0f);
//
//        assertNotNull(products);
//        assertEquals(1, products.size());
//        assertEquals("Test Product", products.get(0).getName());
//    }
//
//    @Test
//    public void testFindByNameContainingIgnoreCase() {
//        productRepository.save(product1);
//        productRepository.save(product2);
//        productRepository.save(product3);
//        productRepository.save(product4);
//
//        List<Product> products = productRepository.findByNameContainingIgnoreCase("test");
//
//        assertNotNull(products);
//        assertEquals(4, products.size());
//    }
//}