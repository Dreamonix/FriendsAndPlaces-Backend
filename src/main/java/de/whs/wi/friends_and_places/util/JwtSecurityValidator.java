package de.whs.wi.friends_and_places.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

/**
 * Validates JWT security settings on application startup.
 * This component checks that the JWT secret meets minimum security requirements.
 */
@Component
public class JwtSecurityValidator {

    private static final int MINIMUM_SECRET_LENGTH = 32;
    private static final Logger logger = LoggerFactory.getLogger(JwtSecurityValidator.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${spring.profiles.active:}")
    private String activeProfile;

    /**
     * Validates the JWT secret meets security requirements.
     * Runs after dependency injection but before the application is fully started.
     */
    @PostConstruct
    public void validateJwtSecret() {
        // Skip strict validation for test profile
        if ("test".equals(activeProfile)) {
            return;
        }

        // Check minimum length
        if (jwtSecret == null || jwtSecret.length() < MINIMUM_SECRET_LENGTH) {
            String errorMsg = "JWT secret is too short! It should be at least " + MINIMUM_SECRET_LENGTH + " characters.";

            if ("prod".equals(activeProfile)) {
                throw new SecurityException(errorMsg);
            } else {
                logger.warn(errorMsg + " This is insecure for production use!");
            }
        }

        // Check for entropy/complexity
        boolean hasUpper = false, hasLower = false, hasDigit = false, hasSpecial = false;

        for (char c : jwtSecret.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else hasSpecial = true;
        }

        int complexityScore = (hasUpper ? 1 : 0) + (hasLower ? 1 : 0) + (hasDigit ? 1 : 0) + (hasSpecial ? 1 : 0);

        if (complexityScore < 3) {
            String warning = "JWT secret has low complexity. It should contain a mix of uppercase, lowercase, digits, and special characters.";

            if ("prod".equals(activeProfile)) {
                throw new SecurityException(warning);
            } else {
                logger.warn(warning);
            }
        }

        // Log success
        logger.info("JWT secret validation completed successfully");
    }
}
