//package com.incon.backend.service;
//
//
//import com.incon.backend.dto.request.ProductRequest;
//import com.incon.backend.dto.response.ProductResponse;
//import com.incon.backend.entity.Product;
//
//import java.util.List;
//
//public interface ProductService {
//
//    ProductResponse createProduct(ProductRequest productRequest, int sellerId);
//
//    List<ProductResponse> getAllProducts();
//
//    ProductResponse getProductById(int productId);
//
//    List<ProductResponse> getProductsByCategory(String category);
//
//    List<ProductResponse> getProductsBySeller(int sellerId);
//
//    ProductResponse updateProduct(int productId, ProductRequest productRequest);
//
//    void deleteProduct(int productId);
//
//    ProductResponse updateProductStock(int productId, int newQuantity);
//
//    ProductResponse updateProductPrice(int productId, float newPrice);
//}