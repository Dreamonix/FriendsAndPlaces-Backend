package de.whs.wi.friends_and_places.error;

/**
 * Exception thrown when input validation fails.
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
