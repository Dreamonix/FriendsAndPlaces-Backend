package de.whs.wi.friends_and_places.controller;

import de.whs.wi.friends_and_places.controller.dto.UserLoginDTO;
import de.whs.wi.friends_and_places.controller.dto.UserRegisterDTO;
import de.whs.wi.friends_and_places.model.User;
import de.whs.wi.friends_and_places.service.UserService;
import de.whs.wi.friends_and_places.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "API endpoints for user registration and authentication")
public class AuthController {

    private final UserService userService;


    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userService = userService;
    }

    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "User already exists",
                    content = @Content) })
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserRegisterDTO newUser) {
        User registeredUser = userService.register(newUser);
        return new ResponseEntity<User>(registeredUser, HttpStatus.OK);
    }

    @Operation(summary = "Login an existing user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "401", description = "Invalid username or password",
                    content = @Content) })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO user) {
        String jwt = userService.authenticate(user);
        return new ResponseEntity<>(jwt, HttpStatus.OK);
    }
}
