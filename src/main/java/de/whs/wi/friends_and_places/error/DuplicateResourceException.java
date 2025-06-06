package de.whs.wi.friends_and_places.error;

/**
 * Exception thrown when duplicate data is detected (e.g., user with existing email).
 */
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
