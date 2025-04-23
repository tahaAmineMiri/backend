//package com.incon.backend.service.impl;
//
//import com.incon.backend.dto.request.CartItemRequest;
//import com.incon.backend.dto.response.CartResponse;
//import com.incon.backend.dto.response.ProductResponse;
//import com.incon.backend.entity.Buyer;
//import com.incon.backend.entity.Cart;
//import com.incon.backend.entity.CartItem;
//import com.incon.backend.entity.Product;
//import com.incon.backend.exception.BadRequestException;
//import com.incon.backend.exception.ResourceNotFoundException;
//import com.incon.backend.repository.BuyerRepository;
//import com.incon.backend.repository.CartItemRepository;
//import com.incon.backend.repository.CartRepository;
//import com.incon.backend.repository.ProductRepository;
//import com.incon.backend.service.CartService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class CartServiceImpl implements CartService {
//
//    private final BuyerRepository buyerRepository;
//    private final CartRepository cartRepository;
//    private final CartItemRepository cartItemRepository;
//    private final ProductRepository productRepository;
//
//    @Autowired
//    public CartServiceImpl(BuyerRepository buyerRepository, CartRepository cartRepository,
//                           CartItemRepository cartItemRepository, ProductRepository productRepository) {
//        this.buyerRepository = buyerRepository;
//        this.cartRepository = cartRepository;
//        this.cartItemRepository = cartItemRepository;
//        this.productRepository = productRepository;
//    }
//
//    @Override
//    public CartResponse getCartByUserId(Integer userId) {
//        Buyer buyer = buyerRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found with id: " + userId));
//
//        Cart cart = buyer.getCart();
//        if (cart == null) {
//            return new   CartResponse(null, 0.0f, null, new ArrayList<>());
//        }
//
//        return mapCartToCartResponse(cart);
//    }
//
//    @Override
//    @Transactional
//    public CartResponse addItemToCart(Integer userId, CartItemRequest request) {
//        Buyer buyer = buyerRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found with id: " + userId));
//
//        Product product = productRepository.findById(request.getProductId())
//                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));
//
//        // Check if stock is sufficient
//        if (product.getStockQuantity() < request.getQuantity()) {
//            throw new BadRequestException("Insufficient stock. Available: " + product.getStockQuantity());
//        }
//
//        // Create cart if it doesn't exist
//        Cart cart = buyer.getCart();
//        if (cart == null) {
//            cart = new Cart();
//            cart.setBuyer(buyer);
//            buyer.setCart(cart);
//            cartRepository.save(cart);
//        }
//
//        // Check if item already exists in cart
//        CartItem cartItem = cartItemRepository
//                .findByCartCartIdAndProductProductId(cart.getCartId(), product.getProductId())
//                .orElse(null);
//
//        if (cartItem != null) {
//            // Update existing item
//            int newQuantity = cartItem.getQuantity() + request.getQuantity();
//            if (product.getStockQuantity() < newQuantity) {
//                throw new BadRequestException("Insufficient stock. Available: " + product.getStockQuantity());
//            }
//
//            cartItem.setQuantity(newQuantity);
//            cartItem.setSubtotal(cartItem.getItemPrice() * newQuantity);
//            cartItemRepository.save(cartItem);
//        } else {
//            // Add new item
//            cartItem = new CartItem();
//            cartItem.setCart(cart);
//            cartItem.setProduct(product);
//            cartItem.setQuantity(request.getQuantity());
//            cartItem.setItemPrice(product.getPrice());
//            cartItem.setSubtotal(product.getPrice() * request.getQuantity());
//            cartItemRepository.save(cartItem);
//        }
//
//        // Update cart total
//        updateCartTotal(cart);
//
//        return mapCartToCartResponse(cart);
//    }
//
//    @Override
//    @Transactional
//    public CartResponse updateCartItem(Integer userId, Integer cartItemId, CartItemRequest request) {
//        Buyer buyer = buyerRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found with id: " + userId));
//
//        Cart cart = buyer.getCart();
//        if (cart == null) {
//            throw new ResourceNotFoundException("Cart not found for buyer with id: " + userId);
//        }
//
//        CartItem cartItem = cartItemRepository.findById(cartItemId)
//                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));
//
//        // Verify that the cart item belongs to the buyer's cart
//        if (!cartItem.getCart().getCartId().equals(cart.getCartId())) {
//            throw new BadRequestException("Cart item does not belong to this buyer");
//        }
//
//        Product product = cartItem.getProduct();
//
//        // Check if stock is sufficient for the update
//        if (product.getStockQuantity() < request.getQuantity()) {
//            throw new BadRequestException("Insufficient stock. Available: " + product.getStockQuantity());
//        }
//
//        cartItem.setQuantity(request.getQuantity());
//        cartItem.setSubtotal(cartItem.getItemPrice() * request.getQuantity());
//        cartItemRepository.save(cartItem);
//
//        // Update cart total
//        updateCartTotal(cart);
//
//        return mapCartToCartResponse(cart);
//    }
//
//    @Override
//    @Transactional
//    public CartResponse removeItemFromCart(Integer userId, Integer productId) {
//        Buyer buyer = buyerRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found with id: " + userId));
//
//        Cart cart = buyer.getCart();
//        if (cart == null) {
//            throw new ResourceNotFoundException("Cart not found for buyer with id: " + userId);
//        }
//
//        CartItem cartItem = cartItemRepository
//                .findByCartCartIdAndProductProductId(cart.getCartId(), productId)
//                .orElseThrow(() -> new ResourceNotFoundException("Product not found in cart: " + productId));
//
//        cartItemRepository.delete(cartItem);
//
//        // Update cart total
//        updateCartTotal(cart);
//
//        return mapCartToCartResponse(cart);
//    }
//
//    @Override
//    @Transactional
//    public void clearCart(Integer userId) {
//        Buyer buyer = buyerRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found with id: " + userId));
//
//        Cart cart = buyer.getCart();
//        if (cart != null) {
//            cart.getCartItems().clear();
//            cart.setTotalAmount(0.0f);
//            cartRepository.save(cart);
//        }
//    }
//
//    private void updateCartTotal(Cart cart) {
//        float total = 0.0f;
//        for (CartItem item : cart.getCartItems()) {
//            total += item.getSubtotal();
//        }
//        cart.setTotalAmount(total);
//        cartRepository.save(cart);
//    }
//
//    private CartResponse mapCartToCartResponse(Cart cart) {
//        List<CartResponse.CartItemResponse> itemResponses = cart.getCartItems().stream()
//                .map(this::mapCartItemToCartItemResponse)
//                .collect(Collectors.toList());
//
//        return new CartResponse(
//                cart.getCartId(),
//                cart.getTotalAmount(),
//                cart.getUpdatedAt(),
//                itemResponses
//        );
//    }
//
//    private CartResponse.CartItemResponse mapCartItemToCartItemResponse(CartItem cartItem) {
//        Product product = cartItem.getProduct();
//        ProductResponse productResponse = new ProductResponse(
//                product.getProductId(),
//                product.getName(),
//                product.getDescription(),
//                product.getPrice(),
//                product.getStockQuantity(),
//                product.getCategory(),
//                product.getImage(),
//                product.getRating()
//        );
//
//        return new CartResponse.CartItemResponse(
//                cartItem.getCartItemId(),
//                productResponse,
//                cartItem.getQuantity(),
//                cartItem.getItemPrice(),
//                cartItem.getSubtotal()
//        );
//    }
//}