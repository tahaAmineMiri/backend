//package com.incon.backend.service;
//
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//
//import com.incon.backend.dto.request.ProductRequest;
//import com.incon.backend.dto.response.ProductResponse;
//import com.incon.backend.entity.Product;
//import com.incon.backend.entity.Seller;
//import com.incon.backend.exception.ResourceNotFoundException;
//import com.incon.backend.repository.ProductRepository;
//import com.incon.backend.repository.SellerRepository;
//import com.incon.backend.service.impl.ProductServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class ProductServiceTest {
//
//    @Mock
//    private ProductRepository productRepository;
//
//    @Mock
//    private SellerRepository sellerRepository;
//
//    @InjectMocks
//    private ProductServiceImpl productService;
//
//    private Product product;
//    private ProductRequest productRequest;
//    private Seller seller;
//    private final int PRODUCT_ID = 1;
//    private final int SELLER_ID = 1;
//
//    @BeforeEach
//    public void setup() {
//        seller = new Seller();
//        seller.setUserId(SELLER_ID);
//        seller.setFullName("Test Seller");
//
//        product = new Product(
//                "Test Product",
//                "Test Description",
//                99.99f,
//                100,
//                "Electronics",
//                "test-image.jpg",
//                seller
//        );
//        product.setProductId(PRODUCT_ID);
//
//        productRequest = new ProductRequest(
//                "Test Product",
//                "Test Description",
//                99.99f,
//                100,
//                "Electronics",
//                "test-image.jpg"
//        );
//    }
//
//    @Test
//    public void testCreateProduct() {
//        // Arrange
//        when(sellerRepository.findById(SELLER_ID)).thenReturn(Optional.of(seller));
//        when(productRepository.save(any(Product.class))).thenReturn(product);
//
//        // Act
//        ProductResponse response = productService.createProduct(productRequest, SELLER_ID);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals(PRODUCT_ID, response.getProductId());
//        assertEquals(productRequest.getName(), response.getName());
//        assertEquals(SELLER_ID, response.getSellerId());
//        verify(productRepository, times(1)).save(any(Product.class));
//    }
//
//    @Test
//    public void testCreateProduct_SellerNotFound() {
//        // Arrange
//        when(sellerRepository.findById(SELLER_ID)).thenReturn(Optional.empty());
//
//        // Act & Assert
//        assertThrows(ResourceNotFoundException.class, () -> {
//            productService.createProduct(productRequest, SELLER_ID);
//        });
//        verify(productRepository, never()).save(any(Product.class));
//    }
//
//    @Test
//    public void testGetAllProducts() {
//        // Arrange
//        Product product2 = new Product(
//                "Another Product",
//                "Another Description",
//                199.99f,
//                50,
//                "Clothing",
//                "another-image.jpg",
//                seller
//        );
//        product2.setProductId(2);
//
//        when(productRepository.findAll()).thenReturn(Arrays.asList(product, product2));
//
//        // Act
//        List<ProductResponse> responses = productService.getAllProducts();
//
//        // Assert
//        assertNotNull(responses);
//        assertEquals(2, responses.size());
//        assertEquals(PRODUCT_ID, responses.get(0).getProductId());
//        assertEquals(2, responses.get(1).getProductId());
//    }
//
//    @Test
//    public void testGetProductById() {
//        // Arrange
//        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
//
//        // Act
//        ProductResponse response = productService.getProductById(PRODUCT_ID);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals(PRODUCT_ID, response.getProductId());
//        assertEquals(product.getName(), response.getName());
//    }
//
//    @Test
//    public void testGetProductById_NotFound() {
//        // Arrange
//        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.empty());
//
//        // Act & Assert
//        assertThrows(ResourceNotFoundException.class, () -> {
//            productService.getProductById(PRODUCT_ID);
//        });
//    }
//
//    @Test
//    public void testUpdateProductStock() {
//        // Arrange
//        int newQuantity = 75;
//        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
//        when(productRepository.save(any(Product.class))).thenReturn(product);
//
//        // Act
//        ProductResponse response = productService.updateProductStock(PRODUCT_ID, newQuantity);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals(newQuantity, response.getStockQuantity());
//        verify(productRepository, times(1)).save(any(Product.class));
//    }
//
//    @Test
//    public void testUpdateProductPrice() {
//        // Arrange
//        float newPrice = 89.99f;
//        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
//        when(productRepository.save(any(Product.class))).thenReturn(product);
//
//        // Act
//        ProductResponse response = productService.updateProductPrice(PRODUCT_ID, newPrice);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals(newPrice, response.getPrice());
//        verify(productRepository, times(1)).save(any(Product.class));
//    }
//
//    @Test
//    public void testDeleteProduct() {
//        // Arrange
//        when(productRepository.existsById(PRODUCT_ID)).thenReturn(true);
//        doNothing().when(productRepository).deleteById(PRODUCT_ID);
//
//        // Act
//        productService.deleteProduct(PRODUCT_ID);
//
//        // Assert
//        verify(productRepository, times(1)).deleteById(PRODUCT_ID);
//    }
//
//    @Test
//    public void testDeleteProduct_NotFound() {
//        // Arrange
//        when(productRepository.existsById(PRODUCT_ID)).thenReturn(false);
//
//        // Act & Assert
//        assertThrows(ResourceNotFoundException.class, () -> {
//            productService.deleteProduct(PRODUCT_ID);
//        });
//        verify(productRepository, never()).deleteById(anyInt());
//    }
//}