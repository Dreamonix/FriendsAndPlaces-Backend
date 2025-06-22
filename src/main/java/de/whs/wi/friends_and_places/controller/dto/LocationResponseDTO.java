package de.whs.wi.friends_and_places.controller.dto;

import de.whs.wi.friends_and_places.model.UserLocation;

import java.time.LocalDateTime;

/**
 * DTO for returning user location data
 */
public class LocationResponseDTO {

    private Long id;
    private double latitude;
    private double longitude;
    private String formattedAddress;
    private LocalDateTime createdAt;
    private String locationName;

    // User information
    private Long userId;
    private String username;
    private String email;

    // Default constructor
    public LocationResponseDTO() {
    }

    // Constructor from entity
    public LocationResponseDTO(UserLocation userLocation) {
        this.id = userLocation.getId();
        this.latitude = userLocation.getLatitude();
        this.longitude = userLocation.getLongitude();
        this.formattedAddress = userLocation.getFormattedAddress();
        this.createdAt = userLocation.getCreatedAt();
        this.locationName = userLocation.getLocationName();

        // Add user information
        if (userLocation.getUser() != null) {
            this.userId = userLocation.getUser().getId();
            this.username = userLocation.getUser().getUsername();
            this.email = userLocation.getUser().getEmail();
        }
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
