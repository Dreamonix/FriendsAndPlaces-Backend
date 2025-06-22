package de.whs.wi.friends_and_places.service.implementations;

import de.whs.wi.friends_and_places.controller.dto.LocationCreateDTO;
import de.whs.wi.friends_and_places.controller.dto.LocationResponseDTO;
import de.whs.wi.friends_and_places.error.ResourceNotFoundException;
import de.whs.wi.friends_and_places.error.ValidationException;
import de.whs.wi.friends_and_places.model.GeocodingData;
import de.whs.wi.friends_and_places.model.User;
import de.whs.wi.friends_and_places.model.UserLocation;
import de.whs.wi.friends_and_places.repository.UserLocationRepository;
import de.whs.wi.friends_and_places.service.FriendService;
import de.whs.wi.friends_and_places.service.GeocodeApiService;
import de.whs.wi.friends_and_places.service.LocationService;
import de.whs.wi.friends_and_places.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing user locations
 */
@Service
public class LocationServiceImpl implements LocationService {

    private static final Logger logger = LoggerFactory.getLogger(LocationServiceImpl.class);

    private final UserLocationRepository locationRepository;
    private final UserService userService;
    private final GeocodeApiService geocodeApiService;
    private final FriendService friendService;

    public LocationServiceImpl(UserLocationRepository locationRepository,
                               UserService userService,
                               GeocodeApiService geocodeApiService,
                               FriendService friendService) {
        this.locationRepository = locationRepository;
        this.userService = userService;
        this.geocodeApiService = geocodeApiService;
        this.friendService = friendService;
    }

    /**
     * Add a new location for a user
     *
     * @param email The username of the user
     * @param locationDTO The location data
     * @return The saved location
     */
    @Transactional
    public LocationResponseDTO addLocation(String email, LocationCreateDTO locationDTO) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));

        UserLocation location;

        // Check if we have coordinates or need to geocode an address
        if (locationDTO.hasCoordinates()) {
            // Coordinate validation is already handled in geocodeApiService.getReverseGeoData()

            // Try to get the address from coordinates using reverse geocoding
            String formattedAddress;
            try {
                GeocodingData geoData = geocodeApiService.getReverseGeoData(
                        locationDTO.getLatitude(),
                        locationDTO.getLongitude()
                );
                formattedAddress = geoData.getFormatted();
            } catch (Exception e) {
                // If reverse geocoding fails, use a placeholder
                logger.warn("Reverse geocoding failed for coordinates: {}, {}",
                        locationDTO.getLatitude(), locationDTO.getLongitude(), e);
                formattedAddress = "Unknown address at coordinates";
            }

            location = new UserLocation(
                    user,
                    locationDTO.getLatitude(),
                    locationDTO.getLongitude(),
                    formattedAddress
            );
        } else if (locationDTO.hasAddress()) {
            // Geocode the address to get coordinates
            try {
                GeocodingData geoData = geocodeApiService.getGeoDataFromAddress(
                        locationDTO.getStreet(),
                        locationDTO.getHousenumber(),
                        locationDTO.getCity(),
                        locationDTO.getCountry()
                );

                location = new UserLocation(
                        user,
                        geoData.getLat(),
                        geoData.getLon(),
                        geoData.getFormatted()
                );
            } catch (Exception e) {
                logger.error("Failed to geocode address", e);
                throw new ValidationException("Could not geocode the provided address");
            }
        } else {
            throw new ValidationException("Either coordinates (latitude/longitude) or a complete address must be provided");
        }

        // Set the optional location name if provided
        if (locationDTO.getLocationName() != null) {
            location.setLocationName(locationDTO.getLocationName());
        }

        UserLocation savedLocation = locationRepository.save(location);
        return new LocationResponseDTO(savedLocation);
    }

    /**
     * Get the latest location for a user
     *
     * @param email The username of the user
     * @return The latest location
     */
    public LocationResponseDTO getLatestLocation(String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));

        UserLocation location = locationRepository.findFirstByUserOrderByCreatedAtDesc(user)
                .orElseThrow(() -> new ResourceNotFoundException("No locations found for user: " + email));

        return new LocationResponseDTO(location);
    }

    /**
     * Get all locations for a user, ordered by creation date (newest first)
     *
     * @param email The username of the user
     * @return List of locations
     */
    public List<LocationResponseDTO> getAllLocations(String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));

        List<UserLocation> locations = locationRepository.findByUserOrderByCreatedAtDesc(user);

        return locations.stream()
                .map(LocationResponseDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Get the latest locations of all friends for a user
     *
     * @param email The username of the user
     * @return List of friends' locations
     */
    public List<LocationResponseDTO> getFriendsLocations(String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));

        List<User> friends = friendService.getFriends(user);

        if (friends.isEmpty()) {
            return List.of(); // Return empty list if no friends
        }

        List<UserLocation> friendLocations = locationRepository.findLatestLocationsByUsers(friends);

        return friendLocations.stream()
                .map(LocationResponseDTO::new)
                .collect(Collectors.toList());
    }
}

