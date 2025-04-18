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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductMapper productMapper;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private ProductRequest productRequest;
    private ProductResponse productResponse;
    private final int PRODUCT_ID = 1;

    @BeforeEach
    public void setup() {
        product = new Product(
                "Test Product",
                "Test Description",
                99.99f,
                100,
                "Electronics",
                "test-image.jpg"
        );
        product.setProductId(PRODUCT_ID);

        productRequest = new ProductRequest(
                "Test Product",
                "Test Description",
                99.99f,
                100,
                "Electronics",
                "test-image.jpg"
        );

        productResponse = new ProductResponse();
        productResponse.setProductId(PRODUCT_ID);
        productResponse.setName("Test Product");
        productResponse.setDescription("Test Description");
        productResponse.setPrice(99.99f);
        productResponse.setStockQuantity(100);
        productResponse.setCategory("Electronics");
        productResponse.setImage("test-image.jpg");
    }

    @Test
    public void testCreateProduct() {
        // Arrange
        // No need for mapToEntity method since it's not in the mapper interface
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toProductResponse(any(Product.class))).thenReturn(productResponse);

        // Act
        ProductResponse response = productService.createProduct(productRequest);

        // Assert
        assertNotNull(response);
        assertEquals(PRODUCT_ID, response.getProductId());
        assertEquals(productRequest.getName(), response.getName());
        verify(productRepository, times(1)).save(any(Product.class));
        verify(productMapper, times(1)).toProductResponse(any(Product.class));
    }

    @Test
    public void testGetAllProducts() {
        // Arrange
        Product product2 = new Product(
                "Another Product",
                "Another Description",
                199.99f,
                50,
                "Clothing",
                "another-image.jpg"
        );
        product2.setProductId(2);

        ProductResponse productResponse2 = new ProductResponse();
        productResponse2.setProductId(2);
        productResponse2.setName("Another Product");
        productResponse2.setDescription("Another Description");
        productResponse2.setPrice(199.99f);
        productResponse2.setStockQuantity(50);
        productResponse2.setCategory("Clothing");
        productResponse2.setImage("another-image.jpg");

        when(productRepository.findAll()).thenReturn(Arrays.asList(product, product2));
        when(productMapper.toProductResponse(product)).thenReturn(productResponse);
        when(productMapper.toProductResponse(product2)).thenReturn(productResponse2);

        // Act
        List<ProductResponse> responses = productService.getAllProducts();

        // Assert
        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals(PRODUCT_ID, responses.get(0).getProductId());
        assertEquals(2, responses.get(1).getProductId());
        verify(productMapper, times(2)).toProductResponse(any(Product.class));
    }

    @Test
    public void testGetProductById() {
        // Arrange
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
        when(productMapper.toProductResponse(product)).thenReturn(productResponse);

        // Act
        ProductResponse response = productService.getProductById(PRODUCT_ID);

        // Assert
        assertNotNull(response);
        assertEquals(PRODUCT_ID, response.getProductId());
        assertEquals(product.getName(), response.getName());
        verify(productMapper, times(1)).toProductResponse(any(Product.class));
    }

    @Test
    public void testGetProductById_NotFound() {
        // Arrange
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            productService.getProductById(PRODUCT_ID);
        });
        verify(productMapper, never()).toProductResponse(any(Product.class));
    }

    @Test
    public void testUpdateProductStock() {
        // Arrange
        int newQuantity = 75;
        Product updatedProduct = new Product(
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                newQuantity,
                product.getCategory(),
                product.getImage()
        );
        updatedProduct.setProductId(PRODUCT_ID);

        ProductResponse updatedResponse = new ProductResponse();
        updatedResponse.setProductId(PRODUCT_ID);
        updatedResponse.setName(product.getName());
        updatedResponse.setDescription(product.getDescription());
        updatedResponse.setPrice(product.getPrice());
        updatedResponse.setStockQuantity(newQuantity);
        updatedResponse.setCategory(product.getCategory());
        updatedResponse.setImage(product.getImage());

        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);
        when(productMapper.toProductResponse(any(Product.class))).thenReturn(updatedResponse);

        // Act
        ProductResponse response = productService.updateProductStock(PRODUCT_ID, newQuantity);

        // Assert
        assertNotNull(response);
        assertEquals(newQuantity, response.getStockQuantity());
        verify(productRepository, times(1)).save(any(Product.class));
        verify(productMapper, times(1)).toProductResponse(any(Product.class));
    }

    @Test
    public void testUpdateProductPrice() {
        // Arrange
        float newPrice = 89.99f;
        Product updatedProduct = new Product(
                product.getName(),
                product.getDescription(),
                newPrice,
                product.getStockQuantity(),
                product.getCategory(),
                product.getImage()
        );
        updatedProduct.setProductId(PRODUCT_ID);

        ProductResponse updatedResponse = new ProductResponse();
        updatedResponse.setProductId(PRODUCT_ID);
        updatedResponse.setName(product.getName());
        updatedResponse.setDescription(product.getDescription());
        updatedResponse.setPrice(newPrice);
        updatedResponse.setStockQuantity(product.getStockQuantity());
        updatedResponse.setCategory(product.getCategory());
        updatedResponse.setImage(product.getImage());

        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);
        when(productMapper.toProductResponse(any(Product.class))).thenReturn(updatedResponse);

        // Act
        ProductResponse response = productService.updateProductPrice(PRODUCT_ID, newPrice);

        // Assert
        assertNotNull(response);
        assertEquals(newPrice, response.getPrice());
        verify(productRepository, times(1)).save(any(Product.class));
        verify(productMapper, times(1)).toProductResponse(any(Product.class));
    }

    @Test
    public void testDeleteProduct() {
        // Arrange
        when(productRepository.existsById(PRODUCT_ID)).thenReturn(true);
        doNothing().when(productRepository).deleteById(PRODUCT_ID);

        // Act
        productService.deleteProduct(PRODUCT_ID);

        // Assert
        verify(productRepository, times(1)).deleteById(PRODUCT_ID);
    }

    @Test
    public void testDeleteProduct_NotFound() {
        // Arrange
        when(productRepository.existsById(PRODUCT_ID)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            productService.deleteProduct(PRODUCT_ID);
        });
        verify(productRepository, never()).deleteById(anyInt());
    }
}