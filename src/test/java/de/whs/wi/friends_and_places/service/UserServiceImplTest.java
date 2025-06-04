package de.whs.wi.friends_and_places.service;

import de.whs.wi.friends_and_places.controller.dto.UserRegisterDTO;
import de.whs.wi.friends_and_places.model.User;
import de.whs.wi.friends_and_places.repository.UserRepository;
import de.whs.wi.friends_and_places.service.implementations.UserServiceImpl;
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

class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setUsername("user");
        dto.setEmail("email@test.com");
        dto.setPassword("pass");
        dto.setCity("city");
        dto.setZipCode("zip");
        dto.setStreet("street");
        dto.setHouseNumber("house");
        dto.setMobile("mobile");
        User user = userService.register(dto);
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

