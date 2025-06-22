package de.whs.wi.friends_and_places.error;

/**
 * Exception thrown when geocoding parameters are invalid.
 */
public class GeocodingValidationException extends ValidationException {
    public GeocodingValidationException(String message) {
        super(message);
    }
}
