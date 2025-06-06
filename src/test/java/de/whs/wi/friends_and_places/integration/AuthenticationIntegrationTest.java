package de.whs.wi.friends_and_places.integration;

import de.whs.wi.friends_and_places.controller.dto.UserLoginDTO;
import de.whs.wi.friends_and_places.controller.dto.UserRegisterDTO;
import de.whs.wi.friends_and_places.model.User;
import de.whs.wi.friends_and_places.repository.UserRepository;
import de.whs.wi.friends_and_places.util.JwtUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthenticationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    private UserRegisterDTO testUser;
    private final String TEST_USERNAME = "testuser";
    private final String TEST_EMAIL = "test@example.com";
    private final String TEST_PASSWORD = "Password123!";

    @BeforeEach
    void setUp() {
        // Set up MockMvc with security
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        testUser = new UserRegisterDTO();
        testUser.setUsername(TEST_USERNAME);
        testUser.setEmail(TEST_EMAIL);
        testUser.setPassword(TEST_PASSWORD);
        testUser.setCity("Test City");
        testUser.setZipCode("12345");
        testUser.setStreet("Test Street");
        testUser.setHouseNumber("123");
        testUser.setMobile("1234567890");
    }

    @AfterEach
    void tearDown() {
        // Clean up the database after each test
        Optional<User> user = userRepository.findByEmail(TEST_EMAIL);
        user.ifPresent(userRepository::delete);
    }

    @Test
    void testCompleteRegistrationAndLoginFlow() throws Exception {
        // 1. Register a new user
        MvcResult registerResult = mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andReturn();

        // Verify user was created in the database
        User savedUser = userRepository.findByEmail(TEST_EMAIL).orElse(null);
        assertNotNull(savedUser, "User should be saved to database after registration");
        assertEquals(TEST_USERNAME, savedUser.getUsername(), "Username should match");
        assertEquals(TEST_EMAIL, savedUser.getEmail(), "Email should match");

        // 2. Login with the created user
        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setEmail(TEST_EMAIL);
        loginDTO.setPassword(TEST_PASSWORD);

        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andReturn();

        // Extract JWT token from response
        String token = loginResult.getResponse().getContentAsString();
        assertNotNull(token, "JWT token should not be null");
        assertFalse(token.isEmpty(), "JWT token should not be empty");

        // 3. Verify token validity
        String username = jwtUtil.extractUsername(token);
        assertEquals(TEST_EMAIL, username, "Token should contain the correct username/email");

        // Verify token is not expired
        assertFalse(jwtUtil.extractExpiration(token).before(new java.util.Date()),
                "Token should not be expired");
    }

    @Test
    void testRegistrationWithExistingUser() throws Exception {
        // First register a user
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk());

        // Try to register the same user again
        // This could either return a 4xx status code or throw an exception
        try {
            MvcResult result = mockMvc.perform(post("/api/v1/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testUser)))
                    .andExpect(status().is4xxClientError())
                    .andReturn();

            // If we get here, verify the error message in the response
            String response = result.getResponse().getContentAsString();
            assertTrue(response.contains("User already exists") ||
                      response.contains("already exists") ||
                      response.contains("duplicate"),
                    "Response should indicate user already exists");
        } catch (Exception e) {
            // Test passes if the exception is a ServletException caused by IllegalArgumentException
            // with message containing "User already exists"
            assertTrue(e instanceof jakarta.servlet.ServletException,
                    "Exception should be a ServletException");
            assertTrue(e.getCause() instanceof java.lang.IllegalArgumentException,
                    "Root cause should be IllegalArgumentException");
            assertTrue(e.getMessage().contains("User already exists") ||
                      e.getCause().getMessage().contains("User already exists"),
                    "Exception should indicate user already exists");
        }
    }

    @Test
    void testInvalidLoginCredentials() throws Exception {
        // Register a user first
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk());

        // Try to login with wrong password
        UserLoginDTO invalidLoginDTO = new UserLoginDTO();
        invalidLoginDTO.setEmail(TEST_EMAIL);
        invalidLoginDTO.setPassword("WrongPassword123!");

        // The login with invalid credentials should result in either a 4xx status code
        // or a BadCredentialsException - both are valid outcomes depending on how
        // the application handles authentication failures
        try {
            mockMvc.perform(post("/api/v1/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidLoginDTO)))
                    .andExpect(status().is4xxClientError());
        } catch (Exception e) {
            // Test passes if the exception is a ServletException caused by BadCredentialsException
            assertTrue(e instanceof jakarta.servlet.ServletException,
                    "Exception should be a ServletException");
            assertTrue(e.getCause() instanceof org.springframework.security.authentication.BadCredentialsException ||
                       e.getMessage().contains("Bad credentials"),
                    "Root cause should be BadCredentialsException or contain 'Bad credentials'");
        }
    }

    @Test
    void testSecurityContextHolderAndJwtToken() throws Exception {
        // 1. Register a new user
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk());

        // 2. Login with the created user
        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setEmail(TEST_EMAIL);
        loginDTO.setPassword(TEST_PASSWORD);

        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andReturn();

        // Extract JWT token from response
        String token = loginResult.getResponse().getContentAsString();
        assertNotNull(token, "JWT token should not be null");

        // 3. Verify the token contains the expected user information
        String username = jwtUtil.extractUsername(token);
        assertEquals(TEST_EMAIL, username, "Token should contain the correct username/email");

        // 4. Verify token is not expired
        assertFalse(jwtUtil.extractExpiration(token).before(new java.util.Date()),
                "Token should not be expired");

        // Note: Once you implement protected endpoints, you can uncomment and
        // adapt the code below to test authorization with the JWT token

        /*
        // Try to access a protected endpoint without token - should be forbidden
        mockMvc.perform(get("/api/v1/your-protected-endpoint"))
                .andExpect(status().isForbidden());

        // Access the same endpoint with token - should be authorized
        mockMvc.perform(get("/api/v1/your-protected-endpoint")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
        */
    }
}
