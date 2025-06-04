package de.whs.wi.friends_and_places.service;

import de.whs.wi.friends_and_places.model.User;
import de.whs.wi.friends_and_places.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        User user = userService.register("user", "email@test.com", "pass", "city", "zip", "street", "house", "mobile");
        assertEquals("user", user.getUsername());
        assertEquals("email@test.com", user.getEmail());
        assertEquals("encodedPassword", user.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testFindByUsername() {
        User user = new User();
        user.setUsername("user");
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        Optional<User> result = userService.findByUsername("user");
        assertTrue(result.isPresent());
        assertEquals("user", result.get().getUsername());
    }

    @Test
    void testFindByEmail() {
        User user = new User();
        user.setEmail("email@test.com");
        when(userRepository.findByEmail("email@test.com")).thenReturn(Optional.of(user));
        Optional<User> result = userService.findByEmail("email@test.com");
        assertTrue(result.isPresent());
        assertEquals("email@test.com", result.get().getEmail());
    }

    @Test
    void testChangePassword() {
        User user = new User();
        when(passwordEncoder.encode("newpass")).thenReturn("encodedNewPass");
        when(userRepository.save(user)).thenReturn(user);
        userService.changePassword(user, "newpass");
        assertEquals("encodedNewPass", user.getPassword());
        verify(userRepository).save(user);
    }
}

