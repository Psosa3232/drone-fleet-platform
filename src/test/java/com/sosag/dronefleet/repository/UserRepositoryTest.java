package com.sosag.dronefleet.repository;

import com.sosag.dronefleet.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the {@link UserRepository}.
 *
 * <p>This test verifies that users can be persisted and retrieved
 * correctly using Spring Data JPA and PostgreSQL.</p>
 */
@SpringBootTest
@Transactional
class UserRepositoryTest {

    /**
     * Repository used to access user data.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Verifies that a user can be saved to the database
     * and subsequently retrieved by its generated identifier.
     */
    @Test
    void shouldSaveAndFindUser() {

        User user = new User();

        user.setName("John");
        user.setSurname("Doe");
        user.setEmail("john.doe@test.com");
        user.setPassword("test-password");
        user.setRole("OPERATOR");
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        assertNotNull(savedUser.getUserId());

        var foundUser = userRepository.findById(savedUser.getUserId());

        assertTrue(foundUser.isPresent());
        assertEquals("john.doe@test.com", foundUser.get().getEmail());
    }
}