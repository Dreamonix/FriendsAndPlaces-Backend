package de.whs.wi.friends_and_places.controller;

import de.whs.wi.friends_and_places.controller.dto.UserLoginDTO;
import de.whs.wi.friends_and_places.controller.dto.UserRegisterDTO;
import de.whs.wi.friends_and_places.error.ApiError;
import de.whs.wi.friends_and_places.model.User;
import de.whs.wi.friends_and_places.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "API endpoints for user registration and authentication")
public class AuthController {

    private final UserService userService;


    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input or missing required fields",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = {@io.swagger.v3.oas.annotations.media.ExampleObject(
                                    name = "Validation Error",
                                    value = "{\"path\":\"/api/v1/auth/register\",\"message\":\"All fields in user registration data must be provided\",\"statusCode\":400,\"statusName\":\"Bad Request\",\"timestamp\":\"2025-06-06T14:30:45.123\",\"errorType\":\"VALIDATION_ERROR\"}"
                            )})}),
            @ApiResponse(responseCode = "409", description = "User already exists with same username or email",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = {@io.swagger.v3.oas.annotations.media.ExampleObject(
                                    name = "Duplicate User",
                                    value = "{\"path\":\"/api/v1/auth/register\",\"message\":\"User with email 'john.doe@example.com' already exists\",\"statusCode\":409,\"statusName\":\"Conflict\",\"timestamp\":\"2025-06-06T14:30:45.123\",\"errorType\":\"CONFLICT\"}"
                            )})})
    })
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserRegisterDTO newUser) {
        User registeredUser = userService.register(newUser);
        return new ResponseEntity<>(registeredUser, HttpStatus.OK);
    }

    @Operation(summary = "Login an existing user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully",
                    content = { @Content(mediaType = "application/json",
                            examples = {@io.swagger.v3.oas.annotations.media.ExampleObject(
                                    name = "JWT Token",
                                    value = "\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huLmRvZUBleGFtcGxlLmNvbSIsImlhdCI6MTY0MzA0MzIwMCwiZXhwIjoxNjQzMDQ2ODAwfQ.jFR2bNS8b4aT5myLBMTdH4ZQHXnrLKH9KYJ1Oah_KQU\""
                            )}) }),
            @ApiResponse(responseCode = "401", description = "Invalid username or password",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = {@io.swagger.v3.oas.annotations.media.ExampleObject(
                                    name = "Authentication Error",
                                    value = "{\"path\":\"/api/v1/auth/login\",\"message\":\"Invalid email or password\",\"statusCode\":401,\"statusName\":\"Unauthorized\",\"timestamp\":\"2025-06-06T14:30:45.123\",\"errorType\":\"AUTHENTICATION_ERROR\"}"
                            )}) })
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO user) {
        String jwt = userService.authenticate(user);
        return new ResponseEntity<>(jwt, HttpStatus.OK);
    }
}
