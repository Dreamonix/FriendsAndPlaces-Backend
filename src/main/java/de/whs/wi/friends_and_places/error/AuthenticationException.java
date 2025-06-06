package de.whs.wi.friends_and_places.error;

/**
 * Exception thrown when authentication fails (invalid credentials, token issues, etc.).
 */
public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }
}
