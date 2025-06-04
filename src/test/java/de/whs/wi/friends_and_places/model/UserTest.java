package de.whs.wi.friends_and_places.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    void testUserGettersAndSetters() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password");
        user.setEmail("test@example.com");
        user.setCity("TestCity");
        user.setZipCode("12345");
        user.setStreet("TestStreet");
        user.setHouseNumber("1A");
        user.setMobile("1234567890");

        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("TestCity", user.getCity());
        assertEquals("12345", user.getZipCode());
        assertEquals("TestStreet", user.getStreet());
        assertEquals("1A", user.getHouseNumber());
        assertEquals("1234567890", user.getMobile());
    }

    @Test
    void testUserAllArgsConstructor() {
        User user = new User(2L, "user2", "pass2", "user2@example.com", "City2", "54321", "Street2", "2B", "0987654321");
        assertEquals(2L, user.getId());
        assertEquals("user2", user.getUsername());
        assertEquals("pass2", user.getPassword());
        assertEquals("user2@example.com", user.getEmail());
        assertEquals("City2", user.getCity());
        assertEquals("54321", user.getZipCode());
        assertEquals("Street2", user.getStreet());
        assertEquals("2B", user.getHouseNumber());
        assertEquals("0987654321", user.getMobile());
    }
}

