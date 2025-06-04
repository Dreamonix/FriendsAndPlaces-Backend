package de.whs.wi.friends_and_places.util;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {
    @InjectMocks
    private JwtUtil jwtUtil;

    private String token;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        String secret = "testsecret";
        ReflectionTestUtils.setField(jwtUtil, "secret", secret);
        // 1 hour
        long expiration = 1000 * 60 * 60;
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationInMs", expiration);
        userDetails = User.withUsername("test@example.com").password("password").roles("USER").build();
        Map<String, Object> claims = new HashMap<>();
        token = Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    @Test
    void testExtractUsername() {
        String username = jwtUtil.extractUsername(token);
        assertEquals(userDetails.getUsername(), username);
    }

    @Test
    void testExtractExpiration() {
        Date expirationDate = jwtUtil.extractExpiration(token);
        assertNotNull(expirationDate);
    }

    @Test
    void testValidateToken() {
        assertTrue(jwtUtil.validateToken(token, userDetails));
    }
}

