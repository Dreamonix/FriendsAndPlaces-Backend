package de.whs.wi.friends_and_places.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/test")
@Tag(name = "Test", description = "Test endpoints requiring authentication")
public class TestController {

    @GetMapping("/secured")
    @Operation(summary = "Test authenticated access", description = "This endpoint requires authentication and returns a simple response")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully authenticated"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - authentication required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Map<String, Object>> securedEndpoint(Principal principal) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Hello World! This is a secured endpoint.");
        response.put("authenticated", true);

        // Include the authenticated user's username if available
        if (principal != null) {
            response.put("username", principal.getName());
            response.put("principalType", principal.getClass().getName());
        } else {
            response.put("principalInfo", "Principal is null");
        }

        // Get authentication details from SecurityContext
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            response.put("authName", auth.getName());
            response.put("authType", auth.getClass().getName());
            response.put("authIsAuthenticated", auth.isAuthenticated());
            // auth.getDetails() is omitted to prevent potential leakage of sensitive information.
        } else {
            response.put("authInfo", "Authentication is null");
        }

        return ResponseEntity.ok(response);
    }
}
