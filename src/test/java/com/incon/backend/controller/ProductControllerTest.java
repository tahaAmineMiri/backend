//package com.incon.backend.controller;
//
//import com.incon.backend.dto.request.ProductRequest;
//import com.incon.backend.dto.response.ApiResponse;
//import com.incon.backend.dto.response.ProductResponse;
//import com.incon.backend.service.ProductService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//public class ProductControllerTest {
//
//    @Mock
//    private ProductService productService;
//
//    @InjectMocks
//    private ProductController productController;
//
//    private ProductRequest productRequest;
//    private ProductResponse productResponse;
//    private List<ProductResponse> productResponseList;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//
//        // Initialize test data
//        productRequest = new ProductRequest();
//        productRequest.setName("Test Product");
//        productRequest.setDescription("This is a test product");
//        productRequest.setPrice(99.99f);
//        productRequest.setStockQuantity(100);
//        productRequest.setCategory("Electronics");
//        productRequest.setImage("test-image.jpg");
//
//        productResponse = new ProductResponse();
//        productResponse.setProductId(1);
//        productResponse.setName("Test Product");
//        productResponse.setDescription("This is a test product");
//        productResponse.setPrice(99.99f);
//        productResponse.setStockQuantity(100);
//        productResponse.setCategory("Electronics");
//        productResponse.setImage("test-image.jpg");
//
//        productResponseList = new ArrayList<>();
//        productResponseList.add(productResponse);
//    }
//
//    @Test
//    public void testCreateProduct() {
//        // Given
//        int sellerId = 1; // Add a sellerId
//        when(productService.createProduct(any(ProductRequest.class), eq(sellerId))).thenReturn(productResponse);
//
//        // When
//        ResponseEntity<ProductResponse> responseEntity = productController.createProduct(productRequest, sellerId);
//
//        // Then
//        assertNotNull(responseEntity);
//        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
//        assertEquals(productResponse, responseEntity.getBody());
//        verify(productService, times(1)).createProduct(any(ProductRequest.class), eq(sellerId));
//    }
//
//    @Test
//    public void testGetAllProducts() {
//        // Given
//        when(productService.getAllProducts()).thenReturn(productResponseList);
//
//        // When
//        ResponseEntity<List<ProductResponse>> responseEntity = productController.getAllProducts();
//
//        // Then
//        assertNotNull(responseEntity);
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(productResponseList, responseEntity.getBody());
//        assertEquals(1, responseEntity.getBody().size());
//        verify(productService, times(1)).getAllProducts();
//    }
//
//    @Test
//    public void testGetProductById() {
//        // Given
//        when(productService.getProductById(anyInt())).thenReturn(productResponse);
//
//        // When
//        ResponseEntity<ProductResponse> responseEntity = productController.getProductById(1);
//
//        // Then
//        assertNotNull(responseEntity);
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(productResponse, responseEntity.getBody());
//        verify(productService, times(1)).getProductById(1);
//    }
//
//    @Test
//    public void testGetProductsByCategory() {
//        // Given
//        when(productService.getProductsByCategory(anyString())).thenReturn(productResponseList);
//
//        // When
//        ResponseEntity<List<ProductResponse>> responseEntity = productController.getProductsByCategory("Electronics");
//
//        // Then
//        assertNotNull(responseEntity);
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(productResponseList, responseEntity.getBody());
//        assertEquals(1, responseEntity.getBody().size());
//        verify(productService, times(1)).getProductsByCategory("Electronics");
//    }
//
//    @Test
//    public void testUpdateProduct() {
//        // Given
//        when(productService.updateProduct(anyInt(), any(ProductRequest.class))).thenReturn(productResponse);
//
//        // When
//        ResponseEntity<ProductResponse> responseEntity = productController.updateProduct(1, productRequest);
//
//        // Then
//        assertNotNull(responseEntity);
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(productResponse, responseEntity.getBody());
//        verify(productService, times(1)).updateProduct(eq(1), any(ProductRequest.class));
//    }
//
//    @Test
//    public void testDeleteProduct() {
//        // Given
//        doNothing().when(productService).deleteProduct(anyInt());
//
//        // When
//        ResponseEntity<ApiResponse> responseEntity = productController.deleteProduct(1);
//
//        // Then
//        assertNotNull(responseEntity);
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals("Product deleted successfully", responseEntity.getBody().getMessage());
//        assertEquals(HttpStatus.OK, responseEntity.getBody().getStatus());
//        verify(productService, times(1)).deleteProduct(1);
//    }
//
//    @Test
//    public void testUpdateProductStock() {
//        // Given
//        when(productService.updateProductStock(anyInt(), anyInt())).thenReturn(productResponse);
//
//        // When
//        ResponseEntity<ProductResponse> responseEntity = productController.updateProductStock(1, 150);
//
//        // Then
//        assertNotNull(responseEntity);
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(productResponse, responseEntity.getBody());
//        verify(productService, times(1)).updateProductStock(1, 150);
//    }
//
//    @Test
//    public void testUpdateProductPrice() {
//        // Given
//        when(productService.updateProductPrice(anyInt(), eq(129.99f))).thenReturn(productResponse);
//
//        // When
//        ResponseEntity<ProductResponse> responseEntity = productController.updateProductPrice(1, 129.99f);
//
//        // Then
//        assertNotNull(responseEntity);
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(productResponse, responseEntity.getBody());
//        verify(productService, times(1)).updateProductPrice(1, 129.99f);
//    }
//}