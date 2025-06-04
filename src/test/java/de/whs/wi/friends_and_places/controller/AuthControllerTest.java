package de.whs.wi.friends_and_places.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.whs.wi.friends_and_places.controller.dto.UserLoginDTO;
import de.whs.wi.friends_and_places.controller.dto.UserRegisterDTO;
import de.whs.wi.friends_and_places.model.User;
import de.whs.wi.friends_and_places.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    private MockMvc mockMvc;
    @Mock
    private UserService userService;
    @InjectMocks
    private AuthController authController;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    @DisplayName("Register user should return 200 and user JSON")
    void registerUser_ReturnsUser() throws Exception {
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setUsername("testuser");
        dto.setEmail("test@example.com");
        dto.setPassword("password");
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        Mockito.when(userService.register(any(UserRegisterDTO.class))).thenReturn(user);

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @DisplayName("Login should return 200 and JWT string")
    void login_ReturnsJwt() throws Exception {
        UserLoginDTO dto = new UserLoginDTO();
        dto.setEmail("test@example.com");
        dto.setPassword("password");
        Mockito.when(userService.authenticate(any(UserLoginDTO.class))).thenReturn("mock-jwt-token");

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("mock-jwt-token"));
    }
}

