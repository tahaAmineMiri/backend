package com.incon.backend.service;

import com.incon.backend.dto.request.ProductRequest;
import com.incon.backend.dto.response.ProductResponse;
import com.incon.backend.entity.Product;
import com.incon.backend.entity.Seller;
import com.incon.backend.exception.InvalidSellerIdException;
import com.incon.backend.exception.ResourceNotFoundException;
import com.incon.backend.mapper.ProductMapper;
import com.incon.backend.repository.ProductRepository;
import com.incon.backend.repository.SellerRepository;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private ProductRequest productRequest;
    private ProductResponse productResponse;
    private Seller seller;

    @BeforeEach
    public void setup() {
        seller = new Seller();
        seller.setId(1);
        seller.setEmail("seller@example.com");

        product = new Product(
                "Test Product",
                "Test Description",
                99.99f,
                100,
                "Electronics",
                "test-image.jpg",
                seller
        );
        product.setProductId(1);

        productRequest = new ProductRequest(
                "Test Product",
                "Test Description",
                99.99f,
                100,
                "Electronics",
                "test-image.jpg"
        );

        productResponse = new ProductResponse();
        productResponse.setProductId(1);
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
        when(sellerRepository.findById(1)).thenReturn(Optional.of(seller));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toProductResponse(any(Product.class))).thenReturn(productResponse);

        // Act
        ProductResponse response = productService.createProduct(productRequest, 1);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getProductId());
        verify(sellerRepository, times(1)).findById(1);
        verify(productRepository, times(1)).save(any(Product.class));
        verify(productMapper, times(1)).toProductResponse(any(Product.class));
    }

    @Test
    public void testCreateProduct_InvalidSellerId() {
        // Arrange
        when(sellerRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> productService.createProduct(productRequest, 1));
        verify(sellerRepository, times(1)).findById(1);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    public void testGetAllProducts() {
        // Arrange
        when(productRepository.findAll()).thenReturn(Arrays.asList(product));
        when(productMapper.toProductResponseList(anyList())).thenReturn(Arrays.asList(productResponse));

        // Act
        List<ProductResponse> responses = productService.getAllProducts();

        // Assert
        assertNotNull(responses);
        assertEquals(1, responses.size());
        verify(productRepository, times(1)).findAll();
        verify(productMapper, times(1)).toProductResponseList(anyList());
    }

    @Test
    public void testGetProductById() {
        // Arrange
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(productMapper.toProductResponse(any(Product.class))).thenReturn(productResponse);

        // Act
        ProductResponse response = productService.getProductById(1);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getProductId());
        verify(productRepository, times(1)).findById(1);
        verify(productMapper, times(1)).toProductResponse(any(Product.class));
    }

    @Test
    public void testGetProductById_NotFound() {
        // Arrange
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(1));
        verify(productRepository, times(1)).findById(1);
    }

    @Test
    public void testUpdateProductStock() {
        // Arrange
        Product updatedProduct = new Product(
                "Test Product",
                "Test Description",
                99.99f,
                50, // New stock quantity
                "Electronics",
                "test-image.jpg",
                seller
        );
        updatedProduct.setProductId(1);

        ProductResponse updatedResponse = new ProductResponse();
        updatedResponse.setProductId(1);
        updatedResponse.setName("Test Product");
        updatedResponse.setDescription("Test Description");
        updatedResponse.setPrice(99.99f);
        updatedResponse.setStockQuantity(50); // New stock quantity
        updatedResponse.setCategory("Electronics");
        updatedResponse.setImage("test-image.jpg");

        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);
        when(productMapper.toProductResponse(any(Product.class))).thenReturn(updatedResponse);

        // Act
        ProductResponse response = productService.updateProductStock(1, 50);

        // Assert
        assertNotNull(response);
        assertEquals(50, response.getStockQuantity());
        verify(productRepository, times(1)).findById(1);
        verify(productRepository, times(1)).save(any(Product.class));
        verify(productMapper, times(1)).toProductResponse(any(Product.class));
    }

    @Test
    public void testUpdateProductPrice() {
        // Arrange
        Product updatedProduct = new Product(
                "Test Product",
                "Test Description",
                79.99f, // New price
                100,
                "Electronics",
                "test-image.jpg",
                seller
        );
        updatedProduct.setProductId(1);

        ProductResponse updatedResponse = new ProductResponse();
        updatedResponse.setProductId(1);
        updatedResponse.setName("Test Product");
        updatedResponse.setDescription("Test Description");
        updatedResponse.setPrice(79.99f); // New price
        updatedResponse.setStockQuantity(100);
        updatedResponse.setCategory("Electronics");
        updatedResponse.setImage("test-image.jpg");

        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);
        when(productMapper.toProductResponse(any(Product.class))).thenReturn(updatedResponse);

        // Act
        ProductResponse response = productService.updateProductPrice(1, 79.99f);

        // Assert
        assertNotNull(response);
        assertEquals(79.99f, response.getPrice());
        verify(productRepository, times(1)).findById(1);
        verify(productRepository, times(1)).save(any(Product.class));
        verify(productMapper, times(1)).toProductResponse(any(Product.class));
    }

    @Test
    public void testDeleteProduct() {
        // Arrange
        when(productRepository.existsById(1)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1);

        // Act
        productService.deleteProduct(1);

        // Assert
        verify(productRepository, times(1)).existsById(1);
        verify(productRepository, times(1)).deleteById(1);
    }

    @Test
    public void testDeleteProduct_NotFound() {
        // Arrange
        when(productRepository.existsById(1)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> productService.deleteProduct(1));
        verify(productRepository, times(1)).existsById(1);
        verify(productRepository, never()).deleteById(anyInt());
    }
}