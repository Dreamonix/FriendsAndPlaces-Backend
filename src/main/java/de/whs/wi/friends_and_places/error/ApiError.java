package de.whs.wi.friends_and_places.error;

import java.time.LocalDateTime;

/**
 * Standardized API error response format.
 * This record represents the structure of error responses sent to clients.
 */
public record ApiError(
        String path,
        String message,
        int statusCode,
        String statusName,
        LocalDateTime timestamp,
        String errorType
) {
    /**
     * Create a new ApiError with the current timestamp.
     */
    public static ApiError create(String path, String message, int statusCode, String statusName, String errorType) {
        return new ApiError(path, message, statusCode, statusName, LocalDateTime.now(), errorType);
    }
}
