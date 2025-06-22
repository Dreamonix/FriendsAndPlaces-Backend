package de.whs.wi.friends_and_places.service;

import de.whs.wi.friends_and_places.controller.dto.LocationCreateDTO;
import de.whs.wi.friends_and_places.controller.dto.LocationResponseDTO;

import java.util.List;

public interface LocationService {

    /**
     * Add a new location for a user.
     *
     * @param email The username of the user.
     * @param locationDTO The location data.
     * @return The saved location.
     */
    LocationResponseDTO addLocation(String email, LocationCreateDTO locationDTO);

    /**
     * Get the latest location for a user.
     *
     * @param email The username of the user.
     * @return The latest location.
     */
    LocationResponseDTO getLatestLocation(String email);

    /**
     * Get all locations for a user.
     *
     * @param email The username of the user.
     * @return A list of all locations.
     */
    List<LocationResponseDTO> getAllLocations(String email);

    /**
     * Get all locations of friends for a user.
     *
     * @param email The username of the user.
     * @return A list of all friends' locations.
     */
    List<LocationResponseDTO> getFriendsLocations(String email);
}
