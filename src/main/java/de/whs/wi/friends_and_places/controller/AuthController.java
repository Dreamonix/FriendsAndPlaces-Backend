package de.whs.wi.friends_and_places.controller;

import de.whs.wi.friends_and_places.controller.dto.UserLoginDTO;
import de.whs.wi.friends_and_places.controller.dto.UserRegisterDTO;
import de.whs.wi.friends_and_places.model.User;
import de.whs.wi.friends_and_places.service.UserService;
import de.whs.wi.friends_and_places.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;


    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserRegisterDTO newUser) {
        User registeredUser = userService.register(newUser);
        return new ResponseEntity<User>(registeredUser, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO user) {
        String jwt = userService.authenticate(user);
        return new ResponseEntity<>(jwt, HttpStatus.OK);
    }
}
