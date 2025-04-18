//package com.incon.backend.controller;
//
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.incon.backend.dto.request.ProductRequest;
//import com.incon.backend.dto.response.ProductResponse;
//import com.incon.backend.service.ProductService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.hamcrest.Matchers.hasSize;
//import static org.hamcrest.Matchers.is;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(controllers = ProductController.class)
//@ExtendWith(MockitoExtension.class)
//public class ProductControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Mock
//    private ProductService productService;
//
//    @InjectMocks
//    private ProductController productController;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private ProductRequest productRequest;
//    private ProductResponse productResponse;
//    private List<ProductResponse> productResponses;
//
//    @BeforeEach
//    public void setup() {
//        productRequest = new ProductRequest(
//                "Test Product",
//                "Test Description",
//                99.99f,
//                100,
//                "Electronics",
//                "test-image.jpg"
//        );
//
//        productResponse = new ProductResponse();
//        productResponse.setProductId(1);
//        productResponse.setName("Test Product");
//        productResponse.setDescription("Test Description");
//        productResponse.setPrice(99.99f);
//        productResponse.setStockQuantity(100);
//        productResponse.setCategory("Electronics");
//        productResponse.setImage("test-image.jpg");
//        productResponse.setRating(0.0f);
//        productResponse.setSellerId(1);
//        productResponse.setSellerName("Test Seller");
//
//        ProductResponse anotherResponse = new ProductResponse();
//        anotherResponse.setProductId(2);
//        anotherResponse.setName("Another Product");
//        anotherResponse.setDescription("Another Description");
//        anotherResponse.setPrice(199.99f);
//        anotherResponse.setStockQuantity(50);
//        anotherResponse.setCategory("Clothing");
//        anotherResponse.setImage("another-image.jpg");
//        anotherResponse.setRating(4.5f);
//        anotherResponse.setSellerId(1);
//        anotherResponse.setSellerName("Test Seller");
//
//        productResponses = Arrays.asList(productResponse, anotherResponse);
//    }
//
//    @Test
//    @WithMockUser(roles = "SELLER")
//    public void testCreateProduct() throws Exception {
//        when(productService.createProduct(any(ProductRequest.class), anyInt())).thenReturn(productResponse);
//
//        mockMvc.perform(post("/api/products")
//                        .param("sellerId", "1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(productRequest)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.productId", is(1)))
//                .andExpect(jsonPath("$.name", is("Test Product")))
//                .andExpect(jsonPath("$.price", is(99.99)));
//    }
//
//    @Test
//    @WithMockUser
//    public void testGetAllProducts() throws Exception {
//        when(productService.getAllProducts()).thenReturn(productResponses);
//
//        mockMvc.perform(get("/api/products"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].productId", is(1)))
//                .andExpect(jsonPath("$[0].name", is("Test Product")))
//                .andExpect(jsonPath("$[1].productId", is(2)))
//                .andExpect(jsonPath("$[1].name", is("Another Product")));
//    }
//
//    @Test
//    @WithMockUser
//    public void testGetProductById() throws Exception {
//        when(productService.getProductById(1)).thenReturn(productResponse);
//
//        mockMvc.perform(get("/api/products/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.productId", is(1)))
//                .andExpect(jsonPath("$.name", is("Test Product")));
//    }
//
//    @Test
//    @WithMockUser
//    public void testGetProductsByCategory() throws Exception {
//        when(productService.getProductsByCategory("Electronics")).thenReturn(Arrays.asList(productResponse));
//
//        mockMvc.perform(get("/api/products/category/Electronics"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("$[0].productId", is(1)))
//                .andExpect(jsonPath("$[0].category", is("Electronics")));
//    }
//
//    @Test
//    @WithMockUser
//    public void testGetProductsBySeller() throws Exception {
//        when(productService.getProductsBySeller(1)).thenReturn(productResponses);
//
//        mockMvc.perform(get("/api/products/seller/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].sellerId", is(1)))
//                .andExpect(jsonPath("$[1].sellerId", is(1)));
//    }
//
//    @Test
//    @WithMockUser(roles = "SELLER")
//    public void testUpdateProduct() throws Exception {
//        when(productService.updateProduct(anyInt(), any(ProductRequest.class))).thenReturn(productResponse);
//
//        mockMvc.perform(put("/api/products/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(productRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.productId", is(1)))
//                .andExpect(jsonPath("$.name", is("Test Product")));
//    }
//
//    @Test
//    @WithMockUser(roles = "SELLER")
//    public void testUpdateProductStock() throws Exception {
//        ProductResponse updatedProduct = new ProductResponse();
//        updatedProduct.setProductId(1);
//        updatedProduct.setName("Test Product");
//        updatedProduct.setStockQuantity(75);
//        updatedProduct.setSellerId(1);
//        updatedProduct.setSellerName("Test Seller");
//
//        when(productService.updateProductStock(1, 75)).thenReturn(updatedProduct);
//
//        mockMvc.perform(patch("/api/products/1/stock")
//                        .param("quantity", "75"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.stockQuantity", is(75)));
//    }
//
//    @Test
//    @WithMockUser(roles = "SELLER")
//    public void testUpdateProductPrice() throws Exception {
//        ProductResponse updatedProduct = new ProductResponse();
//        updatedProduct.setProductId(1);
//        updatedProduct.setName("Test Product");
//        updatedProduct.setPrice(89.99f);
//        updatedProduct.setSellerId(1);
//        updatedProduct.setSellerName("Test Seller");
//
//        when(productService.updateProductPrice(1, 89.99f)).thenReturn(updatedProduct);
//
//        mockMvc.perform(patch("/api/products/1/price")
//                        .param("price", "89.99"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.price", is(89.99)));
//    }
//
//    @Test
//    @WithMockUser(roles = "SELLER")
//    public void testDeleteProduct() throws Exception {
//        doNothing().when(productService).deleteProduct(1);
//
//        mockMvc.perform(delete("/api/products/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success", is(true)))
//                .andExpect(jsonPath("$.message", is("Product deleted successfully")));
//    }
//}