package de.whs.wi.friends_and_places.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a user's location at a specific point in time
 */
@Entity
@Table(name = "user_locations")
public class UserLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(name = "formatted_address")
    private String formattedAddress;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "location_name")
    private String locationName;

    // Default constructor for JPA
    public UserLocation() {
    }

    // Constructor with required fields
    public UserLocation(User user, double latitude, double longitude, String formattedAddress) {
        this.user = user;
        this.latitude = latitude;
        this.longitude = longitude;
        this.formattedAddress = formattedAddress;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
