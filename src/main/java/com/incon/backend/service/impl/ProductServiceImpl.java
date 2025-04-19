package com.incon.backend.service.impl;

import com.incon.backend.dto.request.ProductRequest;
import com.incon.backend.dto.response.ProductResponse;
import com.incon.backend.entity.Product;
import com.incon.backend.exception.ResourceNotFoundException;
import com.incon.backend.repository.ProductRepository;
import com.incon.backend.repository.SellerRepository;
import com.incon.backend.service.ProductService;
import com.incon.backend.mapper.ProductMapper;  // Import the ProductMapper
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;  // Inject ProductMapper

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, SellerRepository sellerRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;  // Initialize the mapper
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = new Product(
                productRequest.getName(),
                productRequest.getDescription(),
                productRequest.getPrice(),
                productRequest.getStockQuantity(),
                productRequest.getCategory(),
                productRequest.getImage()
        );

        Product savedProduct = productRepository.save(product);
        return productMapper.toProductResponse(savedProduct);  // Use mapper
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(productMapper::toProductResponse)  // Use mapper
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse getProductById(int productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        return productMapper.toProductResponse(product);  // Use mapper
    }

    @Override
    public List<ProductResponse> getProductsByCategory(String category) {
        List<Product> products = productRepository.findByCategory(category);
        return products.stream()
                .map(productMapper::toProductResponse)  // Use mapper
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(int productId, ProductRequest productRequest) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setStockQuantity(productRequest.getStockQuantity());
        product.setCategory(productRequest.getCategory());

        if (productRequest.getImage() != null) {
            product.setImage(productRequest.getImage());
        }

        Product updatedProduct = productRepository.save(product);
        return productMapper.toProductResponse(updatedProduct);  // Use mapper
    }

    @Override
    @Transactional
    public void deleteProduct(int productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }
        productRepository.deleteById(productId);
    }

    @Override
    @Transactional
    public ProductResponse updateProductStock(int productId, int newQuantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        product.updateStock(newQuantity);
        Product updatedProduct = productRepository.save(product);
        return productMapper.toProductResponse(updatedProduct);  // Use mapper
    }

    @Override
    @Transactional
    public ProductResponse updateProductPrice(int productId, float newPrice) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        product.updatePrice(newPrice);
        Product updatedProduct = productRepository.save(product);
        return productMapper.toProductResponse(updatedProduct);  // Use mapper
    }
}
