package com.incon.backend.repository;

import com.incon.backend.entity.Seller;
import com.incon.backend.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class SellerRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SellerRepository sellerRepository;

    @Test
    void testFindByEmail_whenSellerExists() {
        // Given
        Seller seller = new Seller();
        seller.setEmail("test@example.com");
        seller.setPassword("password123");
        seller.setRole(Role.SELLER);
        seller.setFullName("Test Seller");
        seller.setVerified(true);

        entityManager.persist(seller);
        entityManager.flush();

        // When
        Optional<Seller> found = sellerRepository.findByEmail("test@example.com");

        // Then
        assertTrue(found.isPresent(), "Seller should be found by email");
        assertEquals("test@example.com", found.get().getEmail(), "Found seller should have matching email");
        assertEquals("Test Seller", found.get().getFullName(), "Found seller should have matching name");
    }

    @Test
    void testFindByEmail_whenSellerDoesNotExist() {
        // When
        Optional<Seller> found = sellerRepository.findByEmail("nonexistent@example.com");

        // Then
        assertFalse(found.isPresent(), "Seller should not be found for non-existent email");
    }

    @Test
    void testExistsByEmail_whenSellerExists() {
        // Given
        Seller seller = new Seller();
        seller.setEmail("exists@example.com");
        seller.setPassword("password123");
        seller.setRole(Role.SELLER);
        seller.setFullName("Existing Seller");

        entityManager.persist(seller);
        entityManager.flush();

        // When
        boolean exists = sellerRepository.existsByEmail("exists@example.com");

        // Then
        assertTrue(exists, "existsByEmail should return true for existing email");
    }

    @Test
    void testExistsByEmail_whenSellerDoesNotExist() {
        // When
        boolean exists = sellerRepository.existsByEmail("nonexistent@example.com");

        // Then
        assertFalse(exists, "existsByEmail should return false for non-existent email");
    }

    @Test
    void testSaveSeller() {
        // Given
        Seller seller = new Seller();
        seller.setEmail("new@example.com");
        seller.setPassword("newpassword");
        seller.setRole(Role.SELLER);
        seller.setFullName("New Seller");
        seller.setPosition("Store Manager");
        seller.setBusinessPhone("123-456-7890");
        seller.setVerified(false);

        // When
        Seller savedSeller = sellerRepository.save(seller);

        // Then
        assertNotNull(savedSeller.getId(), "Saved seller should have an ID");

        // Verify we can retrieve it
        Seller retrievedSeller = entityManager.find(Seller.class, savedSeller.getId());
        assertNotNull(retrievedSeller, "Should be able to retrieve saved seller");
        assertEquals("new@example.com", retrievedSeller.getEmail(), "Retrieved seller should have correct email");
        assertEquals("New Seller", retrievedSeller.getFullName(), "Retrieved seller should have correct name");
    }

    @Test
    void testDeleteSeller() {
        // Given
        Seller seller = new Seller();
        seller.setEmail("delete@example.com");
        seller.setPassword("password");
        seller.setRole(Role.SELLER);
        seller.setFullName("Delete Me");

        seller = entityManager.persist(seller);
        entityManager.flush();

        int sellerId = seller.getId();

        // When
        sellerRepository.delete(seller);
        entityManager.flush();

        // Then
        Seller found = entityManager.find(Seller.class, sellerId);
        assertNull(found, "Seller should be deleted");
    }

    @Test
    void testFindById() {
        // Given
        Seller seller = new Seller();
        seller.setEmail("findbyid@example.com");
        seller.setPassword("password");
        seller.setRole(Role.SELLER);
        seller.setFullName("Find By ID");

        seller = entityManager.persist(seller);
        entityManager.flush();

        // When
        Optional<Seller> found = sellerRepository.findById(seller.getId());

        // Then
        assertTrue(found.isPresent(), "Seller should be found by ID");
        assertEquals("findbyid@example.com", found.get().getEmail(), "Found seller should have matching email");
    }
}