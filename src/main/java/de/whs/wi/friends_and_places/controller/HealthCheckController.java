package de.whs.wi.friends_and_places.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller for health check endpoints
 * Provides information about application status for monitoring systems
 */
@RestController
@RequestMapping("/api/health")
public class HealthCheckController {

    /**
     * Simple health check endpoint that confirms the application is running
     * @return Response with status information
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("service", "friends_and_places");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Detailed health check that provides more system information
     * @return Response with detailed health information
     */
    @GetMapping("/details")
    public ResponseEntity<Map<String, Object>> healthCheckDetailed() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("service", "friends_and_places");
        
        // System information
        Runtime runtime = Runtime.getRuntime();
        Map<String, Object> memory = new HashMap<>();
        memory.put("total", runtime.totalMemory());
        memory.put("free", runtime.freeMemory());
        memory.put("used", runtime.totalMemory() - runtime.freeMemory());
        
        Map<String, Object> system = new HashMap<>();
        system.put("memory", memory);
        system.put("processors", runtime.availableProcessors());
        system.put("javaVersion", System.getProperty("java.version"));
        
        response.put("system", system);
        
        return ResponseEntity.ok(response);
    }
}
