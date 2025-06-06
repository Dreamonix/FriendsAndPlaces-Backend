package de.whs.wi.friends_and_places.error;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * Standardized API error response format.
 * This record represents the structure of error responses sent to clients.
 */
@Schema(description = "Standard error response for API errors")
public record ApiError(
        @Schema(description = "Request path that generated the error", example = "/api/v1/auth/register")
        String path,

        @Schema(description = "Error message details", example = "User with email 'test@example.com' already exists")
        String message,

        @Schema(description = "HTTP status code", example = "409")
        int statusCode,

        @Schema(description = "HTTP status name", example = "Conflict")
        String statusName,

        @Schema(description = "Timestamp when the error occurred", example = "2025-06-06T14:30:45.123")
        LocalDateTime timestamp,

        @Schema(description = "Error type identifier", example = "CONFLICT")
        String errorType
) {
    /**
     * Create a new ApiError with the current timestamp.
     */
    public static ApiError create(String path, String message, int statusCode, String statusName, String errorType) {
        return new ApiError(path, message, statusCode, statusName, LocalDateTime.now(), errorType);
    }
}
