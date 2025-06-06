# Friends and Places (FAP) API

## Overview

Friends and Places is a backend application that manages user registration, login, and location tracking. This API allows users to create accounts, authenticate, and track their locations to connect with friends.

## Features

- **User Management**: Registration and authentication
- **JWT Authentication**: Secure API access with JSON Web Tokens
- **Location Tracking**: Store and retrieve user locations
- **Comprehensive API Documentation**: Interactive OpenAPI/Swagger documentation

## Technology Stack

- Java 17
- Spring Boot 3.5.0
- Spring Security with JWT
- PostgreSQL Database
- Maven
- OpenAPI 3.0 (Swagger) for API documentation

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven
- Docker and Docker Compose (for local development database)

### Running the Application Locally

1. **Start the PostgreSQL database** using Docker Compose:

   ```bash
   docker-compose up -d
   ```

2. **Run the application**:

   ```bash
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
   ```

3. **Access the application**:
   - API base URL: `http://localhost:8080/api/v1`
   - API documentation: `http://localhost:8080/swagger-ui/index.html`

### Stopping the Environment

```bash
docker-compose down
```

To remove all data and start fresh:

```bash
docker-compose down -v
```

## API Documentation

The API is fully documented using OpenAPI (Swagger). This provides an interactive way to explore and test all available endpoints.

### Accessing the Documentation

When the application is running, visit:
```
http://localhost:8080/swagger-ui/index.html
```

### Documentation Features

- Interactive endpoint testing
- Request/response examples
- Authentication documentation
- Error response details

For a comprehensive guide on how to use the API documentation, see:
- [API Documentation Guide](docs/api-documentation.md)

### Additional Documentation

- [Security Documentation](docs/security.md) - Detailed information about JWT authentication, token lifecycle, and security best practices
- [Authentication Testing Guide](docs/authentication-testing.md) - Examples for testing the auth endpoints with cURL and Postman

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── de/whs/wi/friends_and_places/
│   │       ├── config/        # Configuration classes
│   │       ├── controller/    # REST controllers
│   │       ├── dto/           # Data transfer objects
│   │       ├── error/         # Error handling
│   │       ├── model/         # Entity models
│   │       ├── repository/    # Data access
│   │       ├── service/       # Business logic
│   │       └── util/          # Utility classes
│   └── resources/
│       ├── application.yml    # Common application properties
│       ├── application-dev.yml # Development properties
│       └── application-test.yml # Test properties
└── test/                      # Test classes
```

## Development

### Profiles

- **dev**: Development environment with local PostgreSQL database
- **test**: Testing environment with H2 in-memory database
- **prod**: Production environment (configure separately)

### Error Handling

The application uses a standardized error response format for all API errors:

```json
{
  "path": "/api/v1/resource",
  "message": "Error description",
  "statusCode": 400,
  "statusName": "Bad Request",
  "timestamp": "2025-06-06T12:34:56.789",
  "errorType": "ERROR_TYPE"
}
```

## License

[Include license information here]

## Contact

[Include contact information here]
