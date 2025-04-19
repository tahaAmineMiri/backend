package com.incon.backend;

import com.incon.backend.mapper.ProductMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class BackendApplicationTests {

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public ProductMapper productMapper() {
            return Mockito.mock(ProductMapper.class);
        }
    }

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        assertNotNull(applicationContext, "Application context should not be null");
    }

    @Test
    void allBeansAreLoaded() {
        // Verify that specific beans (e.g., controllers, services) are loaded
        assertNotNull(applicationContext.getBean("productController"), "ProductController bean should be loaded");
        assertNotNull(applicationContext.getBean("productServiceImpl"), "ProductServiceImpl bean should be loaded");
        assertNotNull(applicationContext.getBean("productRepository"), "ProductRepository bean should be loaded");
    }
}