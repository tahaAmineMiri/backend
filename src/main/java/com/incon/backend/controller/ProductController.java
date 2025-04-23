package com.incon.backend.controller;


import com.incon.backend.dto.request.ProductRequest;
import com.incon.backend.dto.response.ApiResponse;
import com.incon.backend.dto.response.ProductResponse;
import com.incon.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @PostMapping("/seller/{sellerId}")
//    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest productRequest,
            @PathVariable int sellerId) {
        ProductResponse createdProduct = productService.createProduct(productRequest, sellerId);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable("id") int productId) {
        ProductResponse product = productService.getProductById(productId);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(@PathVariable String category) {
        List<ProductResponse> products = productService.getProductsByCategory(category);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<ProductResponse>> getProductsBySeller(@PathVariable int sellerId) {
        List<ProductResponse> products = productService.getProductsBySeller(sellerId);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable("id") int productId,
            @Valid @RequestBody ProductRequest productRequest) {
        ProductResponse updatedProduct = productService.updateProduct(productId, productRequest);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable("id") int productId) {
        productService.deleteProduct(productId);
        return new ResponseEntity<>(
                new ApiResponse("Product deleted successfully", HttpStatus.OK),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{id}/stock")
//    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ProductResponse> updateProductStock(
            @PathVariable("id") int productId,
            @RequestParam int quantity) {
        ProductResponse updatedProduct = productService.updateProductStock(productId, quantity);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @PatchMapping("/{id}/price")
//    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ProductResponse> updateProductPrice(
            @PathVariable("id") int productId,
            @RequestParam BigDecimal price) {
        ProductResponse updatedProduct = productService.updateProductPrice(productId, price);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }
}
