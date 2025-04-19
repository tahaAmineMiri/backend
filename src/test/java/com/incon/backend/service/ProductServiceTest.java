package com.incon.backend.service;

import com.incon.backend.dto.request.ProductRequest;
import com.incon.backend.dto.response.ProductResponse;
import com.incon.backend.entity.Product;
import com.incon.backend.exception.ResourceNotFoundException;
import com.incon.backend.mapper.ProductMapper;
import com.incon.backend.repository.ProductRepository;
import com.incon.backend.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProduct() {
        ProductRequest productRequest = new ProductRequest("Product1", "Description1", 100.0f, 10, "Category1", "image1");
        Product product = new Product("Product1", "Description1", 100.0f, 10, "Category1", "image1");
        Product savedProduct = new Product("Product1", "Description1", 100.0f, 10, "Category1", "image1");
        ProductResponse productResponse = new ProductResponse(1, "Product1", "Description1", 100.0f, 10, "Category1", "image1", 6);

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);
        when(productMapper.toProductResponse(savedProduct)).thenReturn(productResponse);

        ProductResponse result = productService.createProduct(productRequest, 1);

        assertNotNull(result);
        assertEquals("Product1", result.getName());
        verify(productRepository, times(1)).save(any(Product.class));
        verify(productMapper, times(1)).toProductResponse(savedProduct);
    }

    @Test
    void testGetAllProducts() {
        Product product1 = new Product("Product1", "Description1", 100.0f, 10, "Category1", "image1");
        Product product2 = new Product("Product2", "Description2", 200.0f, 20, "Category2", "image2");
        List<Product> products = Arrays.asList(product1, product2);
        ProductResponse response1 = new ProductResponse(1, "Product1", "Description1", 100.0f, 10, "Category1", "image1", 3);
        ProductResponse response2 = new ProductResponse(2, "Product2", "Description2", 200.0f, 20, "Category2", "image2", 2);

        when(productRepository.findAll()).thenReturn(products);
        when(productMapper.toProductResponse(product1)).thenReturn(response1);
        when(productMapper.toProductResponse(product2)).thenReturn(response2);

        List<ProductResponse> result = productService.getAllProducts();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(productRepository, times(1)).findAll();
        verify(productMapper, times(2)).toProductResponse(any(Product.class));
    }

    @Test
    void testGetProductById_ProductExists() {
        Product product = new Product("Product1", "Description1", 100.0f, 10, "Category1", "image1");
        ProductResponse productResponse = new ProductResponse(1, "Product1", "Description1", 100.0f, 10, "Category1", "image1",5);

        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(productMapper.toProductResponse(product)).thenReturn(productResponse);

        ProductResponse result = productService.getProductById(1);

        assertNotNull(result);
        assertEquals("Product1", result.getName());
        verify(productRepository, times(1)).findById(1);
        verify(productMapper, times(1)).toProductResponse(product);
    }

    @Test
    void testGetProductById_ProductNotFound() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(1));
        verify(productRepository, times(1)).findById(1);
    }
}