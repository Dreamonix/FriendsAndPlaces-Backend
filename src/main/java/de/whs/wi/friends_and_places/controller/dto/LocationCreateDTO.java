package de.whs.wi.friends_and_places.controller.dto;

/**
 * DTO for creating a new user location with coordinates
 */
public class LocationCreateDTO {

    private Double latitude;
    private Double longitude;
    private String locationName;

    // For address-based location input
    private String street;
    private String housenumber;
    private String city;
    private String country;

    // Getters and setters
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHousenumber() {
        return housenumber;
    }

    public void setHousenumber(String housenumber) {
        this.housenumber = housenumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Check if coordinates are provided in this DTO
     */
    public boolean hasCoordinates() {
        return latitude != null && longitude != null;
    }

    /**
     * Check if address is provided in this DTO
     */
    public boolean hasAddress() {
        return street != null && housenumber != null && city != null && country != null;
    }
}
