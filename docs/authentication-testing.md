# Authentication Integration Testing Documentation

## Overview

This document describes the integration tests implemented for the authentication system in the Friends and Places application. These tests verify that the complete registration and login flows work correctly, including JWT token generation and validation.

## Test Coverage

The integration tests focus on the following key aspects of the authentication system:

1. **User Registration**
   - Successful registration with valid data
   - Handling duplicate registration attempts

2. **User Authentication**
   - Login with valid credentials
   - Handling invalid login attempts

3. **JWT Token Management**
   - Generation of JWT tokens upon successful authentication
   - Validation of JWT token contents and expiration

## Test Implementation

The integration tests are implemented in the `AuthenticationIntegrationTest` class, which uses Spring Boot's testing framework with `MockMvc` for simulating HTTP requests.

### Test Environment

- Tests run with the `test` profile activated (`@ActiveProfiles("test")`)
- Database is cleaned up after each test to ensure test isolation
- Tests use real components (repositories, services) but with mocked HTTP requests

### Test Data

A test user is created for each test with the following information:

```java
testUser = new UserRegisterDTO();
testUser.setUsername("testuser");
testUser.setEmail("test@example.com");
testUser.setPassword("Password123!");
testUser.setCity("Test City");
testUser.setZipCode("12345");
testUser.setStreet("Test Street");
testUser.setHouseNumber("123");
testUser.setMobile("1234567890");
```

## Test Cases

### 1. Complete Registration and Login Flow Test

**Method:** `testCompleteRegistrationAndLoginFlow()`

**Purpose:** Verifies the complete authentication flow from registration to login.

**Steps:**
1. Register a new user with valid data
2. Verify the user is created in the database
3. Login with the user's credentials
4. Verify a JWT token is generated
5. Validate the token's username and expiration

**Assertions:**
- User is successfully saved to the database
- Username and email match the registration data
- JWT token is not null or empty
- Token contains the correct username/email
- Token is not expired

### 2. Registration with Existing User Test

**Method:** `testRegistrationWithExistingUser()`

**Purpose:** Verifies that the system properly handles duplicate registration attempts.

**Steps:**
1. Register a new user with valid data
2. Attempt to register the same user again

**Assertions:**
- The second registration attempt fails with an appropriate error
- If the application returns a 4xx status, verify the error message indicates the user already exists
- If the application throws an exception, verify it's an `IllegalArgumentException` with the message "User already exists"

### 3. Invalid Login Credentials Test

**Method:** `testInvalidLoginCredentials()`

**Purpose:** Verifies that the system properly rejects login attempts with invalid credentials.

**Steps:**
1. Register a new user
2. Attempt to login with incorrect password

**Assertions:**
- The login attempt fails
- If the application returns a 4xx status, verify it's an authentication failure
- If the application throws an exception, verify it's a `BadCredentialsException`

### 4. Security Context Holder and JWT Token Test

**Method:** `testSecurityContextHolderAndJwtToken()`

**Purpose:** Verifies that JWT tokens are correctly generated and contain valid information.

**Steps:**
1. Register a new user
2. Login with the user's credentials
3. Extract and validate the JWT token

**Assertions:**
- JWT token is not null
- Token contains the correct username/email
- Token is not expired

## Future Test Enhancements

The test class includes commented code that can be used to test protected endpoints once they are implemented:

```java
// Try to access a protected endpoint without token - should be forbidden
mockMvc.perform(get("/api/v1/your-protected-endpoint"))
        .andExpect(status().isForbidden());

// Access the same endpoint with token - should be authorized
mockMvc.perform(get("/api/v1/your-protected-endpoint")
        .header("Authorization", "Bearer " + token))
        .andExpect(status().isOk());
```

## Running the Tests

The integration tests can be run using Maven:

```bash
mvn test -Dtest=AuthenticationIntegrationTest
```

Or through your IDE's test runner.

## Error Handling

The tests are designed to be robust against different error handling approaches:

1. **Status Code-based Errors:** If the application returns HTTP status codes for errors (e.g., 400 Bad Request, 401 Unauthorized)
2. **Exception-based Errors:** If the application throws exceptions that are propagated to the servlet container

Each test includes appropriate handling for both approaches to ensure the tests pass regardless of how the application implements error handling.

## Best Practices Implemented

1. **Test Isolation:** Each test method is independent and cleans up after itself
2. **Comprehensive Coverage:** Tests cover both successful and error scenarios
3. **Realistic Data:** Tests use realistic test data that mimics actual user input
4. **Flexible Assertions:** Tests can handle different implementation approaches
5. **Clear Documentation:** Each test is well-documented with its purpose, steps, and assertions

## Conclusion

The integration tests provide comprehensive coverage of the authentication system, ensuring that registration, login, and JWT token generation work correctly. These tests serve as both validation of the current implementation and regression protection for future changes.
