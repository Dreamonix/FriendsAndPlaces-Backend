package de.whs.wi.friends_and_places.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the application.
 * This class handles exceptions thrown by controllers and converts them to standardized API responses.
 */
@RestControllerAdvice // Changed from @ControllerAdvice to @RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle resource not found exceptions.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiError> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        ApiError error = ApiError.create(
                request.getRequestURI(),
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                "NOT_FOUND"
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle duplicate resource exceptions.
     */
    @ExceptionHandler(DuplicateResourceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ApiError> handleDuplicateResourceException(
            DuplicateResourceException ex,
            HttpServletRequest request) {

        ApiError error = ApiError.create(
                request.getRequestURI(),
                ex.getMessage(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                "CONFLICT"
        );

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    /**
     * Handle authentication exceptions.
     */
    @ExceptionHandler({AuthenticationException.class, BadCredentialsException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ApiError> handleAuthenticationException(
            RuntimeException ex,
            HttpServletRequest request) {

        ApiError error = ApiError.create(
                request.getRequestURI(),
                ex.getMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                "AUTHENTICATION_ERROR"
        );

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handle custom validation exceptions.
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handleCustomValidationException(
            ValidationException ex,
            HttpServletRequest request) {

        ApiError error = ApiError.create(
                request.getRequestURI(),
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "VALIDATION_ERROR"
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle method argument not valid exceptions.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        String message = "Validation failed: " + errors;
        ApiError error = ApiError.create(
                request.getRequestURI(),
                message,
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "VALIDATION_ERROR"
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle access denied exceptions.
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ApiError> handleAccessDeniedException(
            AccessDeniedException ex,
            HttpServletRequest request) {

        ApiError error = ApiError.create(
                request.getRequestURI(),
                "Access denied: " + ex.getMessage(),
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                "ACCESS_DENIED"
        );

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    /**
     * Handle type mismatch exceptions (e.g., invalid ID format).
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {

        String message = String.format("Parameter '%s' should be of type %s",
                ex.getName(), ex.getRequiredType().getSimpleName());

        ApiError error = ApiError.create(
                request.getRequestURI(),
                message,
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "TYPE_MISMATCH"
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle geocoding validation exceptions.
     */
    @ExceptionHandler(GeocodingValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handleGeocodingValidationException(
            GeocodingValidationException ex,
            HttpServletRequest request) {

        ApiError error = ApiError.create(
                request.getRequestURI(),
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "GEOCODING_VALIDATION_ERROR"
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Fallback handler for any unhandled exceptions.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiError> handleGlobalException(
            Exception ex,
            HttpServletRequest request) {

        ApiError error = ApiError.create(
                request.getRequestURI(),
                "An unexpected error occurred: " + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "INTERNAL_SERVER_ERROR"
        );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
