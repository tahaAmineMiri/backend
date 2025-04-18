package com.incon.backend.repository;

import com.incon.backend.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertNotNull;


// Create a minimal test application config
@SpringBootApplication(scanBasePackages = "com.incon.backend.repository")
@EnableJpaRepositories(basePackages = "com.incon.backend.repository")
@EntityScan(basePackages = "com.incon.backend.entity") // Adjust package if needed
class TestApplication {
}

@DataJpaTest
@ActiveProfiles("test")
@ContextConfiguration(classes = TestApplication.class) // Use test config instead of main app
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;
    Product product1;
    Product product2;

    @BeforeEach
    public void setUp() {
        product1 = new Product("Test Product", "Test Description", 100.0f, 10, "Test Category", "test_image.jpg");
        product2 = new Product("Test Product 2", "Test Description 2", 200.0f, 20, "Test Category 2", "test_image_2.jpg");
    }

    @Test
    public void testSaveProduct() {
        // Your test implementation
        Product productrepository1 = productRepository.save(product1);
        Product productrepository2 = productRepository.save(product2);

        // Verify the product is saved correctly
        assertNotNull(productrepository1);
        assertNotNull(productrepository2);

    }
}