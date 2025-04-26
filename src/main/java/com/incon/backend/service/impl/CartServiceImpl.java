//package com.incon.backend.service.impl;
//
//import com.incon.backend.dto.request.CartItemRequest;
//import com.incon.backend.dto.response.CartResponse;
//import com.incon.backend.entity.Buyer;
//import com.incon.backend.entity.Cart;
//import com.incon.backend.entity.CartItem;
//import com.incon.backend.entity.Product;
//import com.incon.backend.exception.BadRequestException;
//import com.incon.backend.exception.ResourceNotFoundException;
//import com.incon.backend.mapper.CartMapper;
//import com.incon.backend.repository.BuyerRepository;
//import com.incon.backend.repository.CartItemRepository;
//import com.incon.backend.repository.CartRepository;
//import com.incon.backend.repository.ProductRepository;
//import com.incon.backend.service.CartService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//public class CartServiceImpl implements CartService {
//
//    private final BuyerRepository buyerRepository;
//    private final CartRepository cartRepository;
//    private final CartItemRepository cartItemRepository;
//    private final ProductRepository productRepository;
//    private final CartMapper cartMapper;
//
//    @Override
//    @Transactional(readOnly = true)
//    public CartResponse getCartByBuyerId(Integer buyerId) {
//        Buyer buyer = buyerRepository.findById(buyerId)
//                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found with id: " + buyerId));
//
//        Cart cart = buyer.getBuyerCart();
//        if (cart == null) {
//            throw new ResourceNotFoundException("Cart not found for buyer with id: " + buyerId);
//        }
//
//        return cartMapper.toCartResponse(cart);
//    }
//
//    @Override
//    @Transactional
//    public CartResponse addItemToCart(Integer buyerId, CartItemRequest request) {
//        Buyer buyer = buyerRepository.findById(buyerId)
//                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found with id: " + buyerId));
//
//        Product product = productRepository.findById(request.getProductId())
//                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));
//
//        // Check if stock is sufficient
//        if (product.getProductStockQuantity() < request.getCartItemQuantity()) {
//            throw new BadRequestException("Insufficient stock. Available: " + product.getProductStockQuantity());
//        }
//
//        // Get buyer's cart
//        Cart cart = buyer.getBuyerCart();
//        if (cart == null) {
//            cart = new Cart();
//            cart.setCartBuyer(buyer);
//            cart = cartRepository.save(cart);
//            buyer.setBuyerCart(cart);
//            buyerRepository.save(buyer);
//        }
//
//        // Check if item already exists in cart
//        CartItem cartItem = cartItemRepository.findByCartItemCartAndCartItemProduct(cart, product)
//                .orElse(null);
//
//        if (cartItem != null) {
//            // Update existing item
//            int newQuantity = cartItem.getCartItemQuantity() + request.getCartItemQuantity();
//            if (product.getProductStockQuantity() < newQuantity) {
//                throw new BadRequestException("Insufficient stock. Available: " + product.getProductStockQuantity());
//            }
//
//            cartItem.updateQuantity(newQuantity);
//            cartItemRepository.save(cartItem);
//        } else {
//            // Add new item
//            cartItem = new CartItem();
//            cartItem.setCartItemCart(cart);
//            cartItem.setCartItemProduct(product);
//            cartItem.setCartItemQuantity(request.getCartItemQuantity());
//            cartItem.setCartItemPrice(product.getProductPrice());
//            cartItem.setCartItemSubtotal(product.getProductPrice().multiply(java.math.BigDecimal.valueOf(request.getCartItemQuantity())));
//            cartItemRepository.save(cartItem);
//        }
//
//        // Update cart total
//        cart.updateTotalAmount();
//        cartRepository.save(cart);
//
//        return cartMapper.toCartResponse(cart);
//    }
//
//    @Override
//    @Transactional
//    public CartResponse updateCartItem(Integer buyerId, Integer cartItemId, CartItemRequest request) {
//        Buyer buyer = buyerRepository.findById(buyerId)
//                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found with id: " + buyerId));
//
//        Cart cart = buyer.getBuyerCart();
//        if (cart == null) {
//            throw new ResourceNotFoundException("Cart not found for buyer with id: " + buyerId);
//        }
//
//        CartItem cartItem = cartItemRepository.findById(cartItemId)
//                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));
//
//        // Verify that the cart item belongs to the buyer's cart
//        if (!cartItem.getCartItemCart().getCartId().equals(cart.getCartId())) {
//            throw new BadRequestException("Cart item does not belong to this buyer");
//        }
//
//        Product product = cartItem.getCartItemProduct();
//
//        // Check if stock is sufficient for the update
//        if (product.getProductStockQuantity() < request.getCartItemQuantity()) {
//            throw new BadRequestException("Insufficient stock. Available: " + product.getProductStockQuantity());
//        }
//
//        cartItem.updateQuantity(request.getCartItemQuantity());
//        cartItemRepository.save(cartItem);
//
//        // Update cart total
//        cart.updateTotalAmount();
//        cartRepository.save(cart);
//
//        return cartMapper.toCartResponse(cart);
//    }
//
//    @Override
//    @Transactional
//    public CartResponse removeItemFromCart(Integer buyerId, Integer productId) {
//        Buyer buyer = buyerRepository.findById(buyerId)
//                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found with id: " + buyerId));
//
//        Cart cart = buyer.getBuyerCart();
//        if (cart == null) {
//            throw new ResourceNotFoundException("Cart not found for buyer with id: " + buyerId);
//        }
//
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
//
//        CartItem cartItem = cartItemRepository.findByCartItemCartAndCartItemProduct(cart, product)
//                .orElseThrow(() -> new ResourceNotFoundException("Product not found in cart: " + productId));
//
//        cart.removeCartItem(cartItem);
//        cartItemRepository.delete(cartItem);
//        cartRepository.save(cart);
//
//        return cartMapper.toCartResponse(cart);
//    }
//
//    @Override
//    @Transactional
//    public void clearCart(Integer buyerId) {
//        Buyer buyer = buyerRepository.findById(buyerId)
//                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found with id: " + buyerId));
//
//        Cart cart = buyer.getBuyerCart();
//        if (cart != null) {
//            cartItemRepository.deleteAll(cart.getCartItems());
//            cart.getCartItems().clear();
//            cart.updateTotalAmount();
//            cartRepository.save(cart);
//        }
//    }
//}