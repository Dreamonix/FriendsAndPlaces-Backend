package de.whs.wi.friends_and_places.util;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {
    private JwtUtil jwtUtil;

    private String token;
    private UserDetails userDetails;
    private SecretKey secretKey;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Provide the base64 encoded secret string directly
        String secret = "FiJyPNdycju8rMCzfVtH69mS5LCpAQZ4SmBshSt2jLA=";
        long expiration = 3600000; // Example expiration time in milliseconds
        // Manually instantiate JwtUtil with the secret and expiration
        jwtUtil = new JwtUtil(secret, expiration);
        secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        userDetails = User.withUsername("test@example.com").password("password").roles("USER").build();
        token = generateToken(userDetails, new Date(System.currentTimeMillis() + expiration));
    }
    private String generateToken(UserDetails userDetails, Date expirationDate) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(expirationDate)
                .signWith(secretKey)
                .compact();
    }


    @Test
    void extractUsername_validToken_returnsCorrectUsername() {
        String username = jwtUtil.extractUsername(token);
        assertEquals(userDetails.getUsername(), username,
                "The extracted username should match the one used to create the token");
    }

    @Test
    void extractExpiration_validToken_returnsCorrectDate() {
        Date expirationDate = jwtUtil.extractExpiration(token);
        assertNotNull(expirationDate, "Expiration date should not be null");
        assertTrue(expirationDate.after(new Date()),
                "Expiration date should be in the future for a valid token");
    }

    @Test
    void validateToken_validTokenAndMatchingUser_returnsTrue() {
        assertTrue(jwtUtil.validateToken(token, userDetails),
                "Token should be valid for the user it was created for");
    }

    @Test
    void validateToken_expiredToken_throwsException() {
        // Create a token that's already expired
        String expiredToken = generateToken(userDetails, new Date(System.currentTimeMillis() - 1000));

        assertThrows(ExpiredJwtException.class, () -> jwtUtil.validateToken(expiredToken, userDetails),
                "An expired token should throw ExpiredJwtException");
    }

    @Test
    void validateToken_invalidSignature_throwsException() {
        // Tamper with the token by changing the last character
        String tamperedToken = token.substring(0, token.length() - 1) + "X";

        assertThrows(SignatureException.class, () -> jwtUtil.validateToken(tamperedToken, userDetails),
                "A token with invalid signature should throw SignatureException");
    }

    @Test
    void validateToken_malformedToken_throwsException() {
        // Create an obviously malformed token
        String malformedToken = "not.a.jwt.token";

        assertThrows(MalformedJwtException.class, () -> jwtUtil.validateToken(malformedToken, userDetails),
                "A malformed token should throw MalformedJwtException");
    }

    @Test
    void validateToken_wrongUser_returnsFalse() {
        // Create a different user
        UserDetails differentUser = User.withUsername("other@example.com")
                .password("password").roles("USER").build();

        assertFalse(jwtUtil.validateToken(token, differentUser),
                "Token should be invalid for a user different from the one it was created for");
    }
}
