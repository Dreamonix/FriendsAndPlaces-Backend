# Authentication Testing Guide

This document provides examples of how to test the authentication endpoints using different tools.

## Testing with cURL

### User Registration

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "securePassword123",
    "city": "Berlin",
    "zipCode": "10115",
    "street": "Teststraße",
    "houseNumber": "42",
    "mobile": "+491234567890"
  }'
```

### User Login

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "securePassword123"
  }'
```

The response will contain a JWT token that should be included in subsequent requests:

```json
"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0QGV4YW1wbGUuY29tIiwiaWF0IjoxNjQ0MzM0NTY3LCJleHAiOjE2NDQzMzgxNjd9.8Tj1HZhSAZ_IbY3OsP8JXYcVViLKRF0VsEKlA-1G5XA"
```

### Accessing Protected Resources

```bash
curl -X GET http://localhost:8080/api/v1/some-protected-endpoint \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0QGV4YW1wbGUuY29tIiwiaWF0IjoxNjQ0MzM0NTY3LCJleHAiOjE2NDQzMzgxNjd9.8Tj1HZhSAZ_IbY3OsP8JXYcVViLKRF0VsEKlA-1G5XA"
```

## Testing with Postman

1. **Setup a Collection**:
   - Create a new collection named "Friends and Places API"
   - Add a variable called `baseUrl` with value `http://localhost:8080`
   - Add a variable called `token` (will be populated after login)

2. **Register Request**:
   - Method: POST
   - URL: {{baseUrl}}/api/v1/auth/register
   - Body (raw JSON):
     ```json
     {
       "username": "postmanuser",
       "email": "postman@example.com",
       "password": "securePassword123",
       "city": "Berlin",
       "zipCode": "10115",
       "street": "Postmanstraße",
       "houseNumber": "123",
       "mobile": "+491234567890"
     }
     ```

3. **Login Request**:
   - Method: POST
   - URL: {{baseUrl}}/api/v1/auth/login
   - Body (raw JSON):
     ```json
     {
       "email": "postman@example.com",
       "password": "securePassword123"
     }
     ```
   - Tests tab (to automatically set the token variable):
     ```javascript
     var jsonData = pm.response.json();
     pm.environment.set("token", jsonData);
     ```

4. **Protected Endpoint Request**:
   - Method: GET
   - URL: {{baseUrl}}/api/v1/some-protected-endpoint
   - Authorization tab: Bearer Token, value: {{token}}

## Common Issues

1. **401 Unauthorized**:
   - Check if you're using the correct email/password
   - Verify token hasn't expired (default: 1 hour)
   - Ensure token is properly formatted in the Authorization header

2. **400 Bad Request**:
   - Check request body format
   - Ensure all required fields are provided
   - Verify email format is valid

3. **409 Conflict**:
   - Username or email already exists
   - Try registering with different credentials
