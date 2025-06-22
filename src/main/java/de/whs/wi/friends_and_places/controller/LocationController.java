package de.whs.wi.friends_and_places.controller;

import de.whs.wi.friends_and_places.controller.dto.LocationCreateDTO;
import de.whs.wi.friends_and_places.controller.dto.LocationResponseDTO;
import de.whs.wi.friends_and_places.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing user locations
 */
@RestController
@RequestMapping("/api/v1/places")
@Tag(name = "Locations", description = "Operations for managing user locations")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PutMapping
    @Operation(summary = "Log a new location",
               description = "Create a new location entry for the authenticated user. " +
                             "Provide either latitude/longitude coordinates or a complete address.")
    public ResponseEntity<LocationResponseDTO> addLocation(
            Authentication authentication,
            @RequestBody LocationCreateDTO locationDTO) {

        String username = authentication.getName();
        LocationResponseDTO location = locationService.addLocation(username, locationDTO);
        return ResponseEntity.ok(location);
    }

    @GetMapping("/latest")
    @Operation(summary = "Get latest location",
               description = "Retrieve the most recent location for the authenticated user")
    public ResponseEntity<LocationResponseDTO> getLatestLocation(Authentication authentication) {
        String username = authentication.getName();
        LocationResponseDTO location = locationService.getLatestLocation(username);
        return ResponseEntity.ok(location);
    }

    @GetMapping
    @Operation(summary = "Get all locations",
               description = "Retrieve all locations for the authenticated user, sorted by date (newest first)")
    public ResponseEntity<List<LocationResponseDTO>> getAllLocations(Authentication authentication) {
        String username = authentication.getName();
        List<LocationResponseDTO> locations = locationService.getAllLocations(username);
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/friends")
    @Operation(summary = "Get friends' locations",
               description = "Retrieve the latest locations of all friends of the authenticated user")
    public ResponseEntity<List<LocationResponseDTO>> getFriendsLocations(Authentication authentication) {
        String username = authentication.getName();
        List<LocationResponseDTO> friendsLocations = locationService.getFriendsLocations(username);
        return ResponseEntity.ok(friendsLocations);
    }
}

