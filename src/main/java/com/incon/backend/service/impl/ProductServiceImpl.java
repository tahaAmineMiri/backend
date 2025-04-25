package com.incon.backend.service.impl;

import com.incon.backend.dto.request.ProductRequest;
import com.incon.backend.dto.response.ProductResponse;
import com.incon.backend.entity.Product;
import com.incon.backend.entity.Seller;
import com.incon.backend.exception.InvalidSellerIdException;
import com.incon.backend.exception.ResourceNotFoundException;
import com.incon.backend.repository.ProductRepository;
import com.incon.backend.repository.SellerRepository;
import com.incon.backend.service.ProductService;
import com.incon.backend.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final SellerRepository sellerRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, SellerRepository sellerRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.sellerRepository = sellerRepository;
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest, int sellerId) {
        if (sellerId <= 0) {
            throw new InvalidSellerIdException("Invalid sellerId: " + sellerId);
        }

        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with id: " + sellerId));

        Product product = new Product(
                productRequest.getProductName(),
                productRequest.getProductDescription(),
                productRequest.getProductPrice(),
                productRequest.getProductStockQuantity(),
                productRequest.getProductCategory(),
                productRequest.getProductImage(),
                seller
        );

        Product savedProduct = productRepository.save(product);
        return productMapper.toProductResponse(savedProduct);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return productMapper.toProductResponseList(products);
    }

    @Override
    public ProductResponse getProductById(int productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        return productMapper.toProductResponse(product);
    }

    @Override
    public List<ProductResponse> getProductsByCategory(String productCategory) {
        List<Product> products = productRepository.findByProductCategory(productCategory);
        return productMapper.toProductResponseList(products);
    }

    @Override
    public List<ProductResponse> getProductsBySeller(int sellerId) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with id: " + sellerId));

        List<Product> products = productRepository.findByProductSeller(seller);

        return productMapper.toProductResponseList(products);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(int productId, ProductRequest productRequest) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        productMapper.updateProductFromRequest(productRequest, product);

        Product updatedProduct = productRepository.save(product);
        return productMapper.toProductResponse(updatedProduct);
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
        return productMapper.toProductResponse(updatedProduct);
    }

    @Override
    @Transactional
    public ProductResponse updateProductPrice(int productId, BigDecimal newPrice) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        product.updatePrice(newPrice);
        Product updatedProduct = productRepository.save(product);
        return productMapper.toProductResponse(updatedProduct);
    }
}