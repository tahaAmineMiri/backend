package com.incon.backend.repository;

import com.incon.backend.entity.Seller;
import com.incon.backend.enums.Role;
import org.junit.jupiter.api.BeforeEach;
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

    private Seller seller;

    @BeforeEach
    void setUp() {
        seller = new Seller();
        seller.setEmail("test@example.com");
        seller.setPassword("password123");
        seller.setRole(Role.SELLER);
        seller.setFullName("Test Seller");
        seller.setPosition("Manager");
        seller.setBusinessPhone("123-456-7890");
        seller.setVerified(true);
    }

    @Test
    void testFindByEmail_whenSellerExists() {
        entityManager.persistAndFlush(seller);

        Optional<Seller> found = sellerRepository.findByEmail("test@example.com");

        assertTrue(found.isPresent(), "Seller should be found by email");
        assertEquals("test@example.com", found.get().getEmail());
    }

    @Test
    void testFindByEmail_whenSellerDoesNotExist() {
        Optional<Seller> found = sellerRepository.findByEmail("nonexistent@example.com");

        assertFalse(found.isPresent(), "Seller should not be found for non-existent email");
    }

    @Test
    void testExistsByEmail_whenSellerExists() {
        entityManager.persistAndFlush(seller);

        boolean exists = sellerRepository.existsByEmail("test@example.com");

        assertTrue(exists, "existsByEmail should return true for existing email");
    }

    @Test
    void testExistsByEmail_whenSellerDoesNotExist() {
        boolean exists = sellerRepository.existsByEmail("nonexistent@example.com");

        assertFalse(exists, "existsByEmail should return false for non-existent email");
    }

    @Test
    void testSaveSeller() {
        Seller savedSeller = sellerRepository.save(seller);

        assertNotNull(savedSeller, "Saved seller should have an ID");
        assertEquals("test@example.com", savedSeller.getEmail());
    }

    @Test
    void testDeleteSeller() {
        Seller savedSeller = entityManager.persistAndFlush(seller);

        sellerRepository.delete(savedSeller);
        entityManager.flush();

        Seller found = entityManager.find(Seller.class, savedSeller.getId());
        assertNull(found, "Seller should be deleted");
    }

    @Test
    void testFindById() {
        Seller savedSeller = entityManager.persistAndFlush(seller);

        Optional<Seller> found = sellerRepository.findById(savedSeller.getId());

        assertTrue(found.isPresent(), "Seller should be found by ID");
        assertEquals("test@example.com", found.get().getEmail());
    }
}