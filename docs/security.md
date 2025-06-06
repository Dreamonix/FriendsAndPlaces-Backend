# Security Documentation

## Overview

This document outlines the security measures implemented in the Friends and Places API, focusing on authentication, authorization, and data protection.

## Authentication

### JWT (JSON Web Tokens)

The application uses JSON Web Tokens for stateless authentication:

- **Token Generation**: Upon successful login, the server generates a JWT containing the user's identity
- **Token Structure**: 
  - Header: Contains the algorithm used for signing
  - Payload: Contains claims about the user (subject, expiration time)
  - Signature: Ensures the token hasn't been tampered with
- **Token Response Format**: JWT tokens are returned in JSON format: `{"token": "jwt-token-value"}`

### Token Lifecycle

1. **Generation**: Created during successful login
2. **Expiration**: Tokens expire after 1 hour (configurable in application properties)
3. **Validation**: Performed on each protected request
4. **Storage**: Clients should store tokens securely (e.g., in HttpOnly cookies or secure storage)

### Security Considerations

- **Token Transmission**: Always use HTTPS in production to prevent token interception
- **Secret Key Management**: 
  - The JWT secret in application-dev.yml is for development only
  - In production, use environment variables or a secure vault
  - Rotate secrets periodically

### Error Handling

The JWT authentication filter provides detailed error messages for authentication failures:
- Invalid token format
- Token expired
- Signature validation failed
- User not found

## Authorization

The application implements role-based access control:
- Regular users can access their own data
- Authorization is enforced at both controller and service layers

## Testing Authentication

A dedicated test endpoint is available for verifying authentication:
- Endpoint: `/api/v1/test/secured`
- Requires a valid JWT token
- Returns user information and authentication status
- Useful for troubleshooting authentication issues

## Error Handling

Security-related errors return standardized responses:
- 401 Unauthorized: Invalid or missing authentication
- 403 Forbidden: Authenticated but insufficient permissions
- Response bodies include helpful messages without exposing sensitive information

## Data Protection

- Passwords are hashed using BCrypt before storage
- Personal information is only accessible to authorized users
- Database credentials are externalized and should be secured in production

## Development vs. Production

### Development Environment
- Less restrictive CORS settings
- More verbose error messages
- In-memory or local database
- Extended token expiration (24 hours)

### Production Recommendations
- Restrict CORS to known origins
- Limit error message details
- Use environment variables for all secrets
- Enable rate limiting
- Consider adding audit logging
- Reduce token expiration time to appropriate value for your use case
