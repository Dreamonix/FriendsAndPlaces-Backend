# Geocoding Service Documentation

## Overview

The Geocoding Service provides a robust interface for interacting with external geographic data providers. This document explains how to use the geocoding functionality within the Friends and Places application, covering its API methods, configuration, and best practices.

## Features

The Geocoding Service offers the following key features:

- **Postal Code Lookup**: Convert postal/zip codes to detailed geographic information
- **Address Geocoding**: Convert street addresses to geographic coordinates and location details
- **Reverse Geocoding**: Convert latitude/longitude coordinates to human-readable addresses
- **Rich Location Data**: Access detailed location information including administrative divisions, timezones, and more

## Configuration

The Geocoding Service uses an external API provider (Geoapify) and can be configured through application properties.

### API Configuration

Add the following properties to your `application.yml` file:

```yaml
# External API Configuration
external-api:
  base-url: https://api.geoapify.com/v1
  connection-timeout: 5000
  read-timeout: 10000
```

For environment-specific configuration (such as API keys), add the following to your environment-specific files (e.g., `application-dev.yml`):

```yaml
# External API Configuration for Development
external-api:
  api-key: your-api-key-here # Replace with your actual API key
```

## Service Interface

The `GeocodeApiService` interface provides methods for interacting with geocoding functionality:

```java
public interface GeocodeApiService {
    /**
     * Retrieves geographic data for a specific zip code
     */
    GeocodingData getGeoDataFromZipCode(String zipCode);

    /**
     * Retrieves geographic data for a specific street address
     */
    GeocodingData getGeoDataFromAddress(String street, String housenumber, String city, String country);

    /**
     * Performs reverse geocoding to retrieve address information for specific coordinates
     */
    GeocodingData getReverseGeoData(double latitude, double longitude);
}
```

## Data Model

The `GeocodingData` model contains detailed location information returned by the geocoding API:

- **Basic Location Information**: country, state, city, postal code, coordinates
- **Address Details**: street, house number, formatted address
- **Administrative Information**: country code, state code, county information
- **Timezone Information**: timezone name, UTC offsets, abbreviations
- **Geographical Data**: bounding box coordinates, location category

## Usage Examples

### Postal Code Lookup

```java
@Autowired
private GeocodeApiService geocodeApiService;

public void processLocation(String zipCode) {
    try {
        GeocodingData geoData = geocodeApiService.getGeoDataFromZipCode(zipCode);
        System.out.println("City: " + geoData.getCity());
        System.out.println("Coordinates: " + geoData.getLat() + ", " + geoData.getLon());
    } catch (ResourceNotFoundException e) {
        System.out.println("Location not found for zip code: " + zipCode);
    }
}
```

### Address Geocoding

```java
@Autowired
private GeocodeApiService geocodeApiService;

public void processAddress(String street, String housenumber, String city, String country) {
    try {
        GeocodingData geoData = geocodeApiService.getGeoDataFromAddress(street, housenumber, city, country);
        System.out.println("Formatted Address: " + geoData.getFormatted());
        System.out.println("Coordinates: " + geoData.getLat() + ", " + geoData.getLon());
    } catch (ResourceNotFoundException e) {
        System.out.println("Location not found for address");
    }
}
```

### Reverse Geocoding

```java
@Autowired
private GeocodeApiService geocodeApiService;

public void processCoordinates(double latitude, double longitude) {
    try {
        GeocodingData geoData = geocodeApiService.getReverseGeoData(latitude, longitude);
        System.out.println("Address: " + geoData.getFormatted());
        System.out.println("Country: " + geoData.getCountry());
        System.out.println("City: " + geoData.getCity());
    } catch (ResourceNotFoundException e) {
        System.out.println("Address not found for coordinates");
    }
}
```

## Error Handling

The Geocoding Service provides robust error handling for various scenarios:

- **ResourceNotFoundException**: Thrown when no data is found for the provided input
- **RuntimeException**: Thrown for API communication errors, timeout issues, or parsing problems

Example error handling:

```java
try {
    GeocodingData geoData = geocodeApiService.getGeoDataFromZipCode("12345");
    // Process data
} catch (ResourceNotFoundException e) {
    // Handle not found scenario
    logger.warn("Location not found: {}", e.getMessage());
} catch (RuntimeException e) {
    // Handle API errors
    logger.error("API error: {}", e.getMessage());
}
```

## Best Practices

1. **Cache Results**: Consider caching geocoding results to reduce API calls for frequently accessed locations
2. **Validate Input**: Validate postal codes and addresses before making API calls
3. **Handle Errors Gracefully**: Implement proper error handling to manage API failures
4. **Respect Rate Limits**: Be aware of the external API's rate limits and design your application accordingly
5. **Secure API Keys**: Always store API keys in environment-specific configuration files or environment variables

## API Provider Information

The current implementation uses [Geoapify](https://www.geoapify.com/) as the geocoding provider. Their documentation can be found at [https://apidocs.geoapify.com/](https://apidocs.geoapify.com/).
