//package com.incon.backend.integration;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.incon.backend.dto.request.ProductRequest;
//import com.incon.backend.dto.response.ProductResponse;
//import com.incon.backend.entity.Seller;
//import com.incon.backend.repository.SellerRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
////@Sql(scripts = "/test-data.sql") // Optional: Load test data
//public class ProductIntegrationTest {
//
//    @Autowired
//    private WebApplicationContext webApplicationContext;
//
//    @Autowired
//    private SellerRepository sellerRepository;
//
//    private MockMvc mockMvc;
//    private ObjectMapper objectMapper;
//    private Seller seller;
//
//    @BeforeEach
//    public void setup() {
//        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//        objectMapper = new ObjectMapper();
//
//        // Create a test seller
//        seller = new Seller();
//        seller.setEmail("seller@example.com");
//        seller.setPassword("password123");
//        seller.setFullName("Test Seller");
//        seller.setPosition("Manager");
//        seller.setBusinessPhone("1234567890");
//        seller.setRole(com.incon.backend.enums.Role.SELLER);
//        seller.setVerified(true);
//        sellerRepository.save(seller);
//    }
//
//    @Test
//    public void testCreateProduct() throws Exception {
//        ProductRequest productRequest = new ProductRequest(
//                "Test Product", "Description", 100.0f, 10, "Category", "image.jpg"
//        );
//
//        mockMvc.perform(post("/api/products?sellerId=" + seller.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(productRequest)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.name").value("Test Product"))
//                .andExpect(jsonPath("$.price").value(100.0));
//    }
//
//    @Test
//    public void testGetAllProducts() throws Exception {
//        mockMvc.perform(get("/api/products"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").isNumber());
//    }
//
//    @Test
//    public void testGetProductById() throws Exception {
//        ProductResponse product = createTestProduct();
//
//        mockMvc.perform(get("/api/products/" + product.getProductId()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value(product.getName()));
//    }
//
//    private ProductResponse createTestProduct() {
//        ProductRequest productRequest = new ProductRequest(
//                "Test Product", "Description", 100.0f, 10, "Category", "image.jpg"
//        );
//        return new ProductResponse(1, "Test Product", "Description", 100.0f, 10, "Category", "image.jpg", 0.0f);
//    }
//}