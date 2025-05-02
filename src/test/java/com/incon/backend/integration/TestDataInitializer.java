package com.incon.backend.integration;

import com.incon.backend.entity.*;
import com.incon.backend.enums.Role;
import com.incon.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Initializes test data for integration tests.
 * Only active in the "test" profile.
 */
@Component
@Profile("test")
public class TestDataInitializer implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        // Create test admin
        Admin admin = new Admin();
        admin.setAdminName("Test Admin");
        admin.setAdminEmail("admin@test.com");
        admin.setAdminPassword(passwordEncoder.encode("password"));
        admin.setAdminRole("ADMIN");

        // Create test buyer
        Buyer buyer = new Buyer();
        buyer.setUserEmail("buyer@test.com");
        buyer.setUserPassword(passwordEncoder.encode("password"));
        buyer.setUserFullName("Test Buyer");
        buyer.setUserPosition("Manager");
        buyer.setUserBusinessPhone("1234567890");
        buyer.setUserRole(Role.BUYER);
        buyer.setUserIsVerified(true);
        buyer.setBuyerOrders(new ArrayList<>());

        Buyer savedBuyer = buyerRepository.save(buyer);

        // Create cart for buyer
        Cart cart = new Cart();
        cart.setCartBuyer(savedBuyer);
        cart.setCartTotalAmount(BigDecimal.ZERO);
        cart.setCartItems(new ArrayList<>());

        Cart savedCart = cartRepository.save(cart);

        savedBuyer.setBuyerCart(savedCart);
        buyerRepository.save(savedBuyer);

        // Create test seller
        Seller seller = new Seller();
        seller.setUserEmail("seller@test.com");
        seller.setUserPassword(passwordEncoder.encode("password"));
        seller.setUserFullName("Test Seller");
        seller.setUserPosition("Owner");
        seller.setUserBusinessPhone("9876543210");
        seller.setUserRole(Role.SELLER);
        seller.setUserIsVerified(true);
        seller.setSellerProducts(new ArrayList<>());

        Seller savedSeller = sellerRepository.save(seller);

        // Create test products
        for (int i = 1; i <= 3; i++) {
            Product product = new Product();
            product.setProductName("Test Product " + i);
            product.setProductDescription("Description for test product " + i);
            product.setProductPrice(new BigDecimal("" + (i * 10 + 9.99)));
            product.setProductStockQuantity(50 * i);
            product.setProductCategory(i % 2 == 0 ? "Electronics" : "Home");
            product.setProductImage("http://example.com/image" + i + ".jpg");
            product.setProductSeller(savedSeller);
            product.setProductRating(0.0f);
            product.setProductReviews(new ArrayList<>());

            productRepository.save(product);
        }
    }
}