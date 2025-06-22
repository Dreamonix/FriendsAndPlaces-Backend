package de.whs.wi.friends_and_places.service;

import de.whs.wi.friends_and_places.model.GeocodingData;

/**
 * Service interface for external geocoding API operations
 */
public interface GeocodeApiService {

    /**
     * Retrieves geographic data for a specific zip code
     *
     * @param zipCode The zip/postal code to get geographic data for
     * @return GeocodingData containing geographic information from Geoapify
     * @throws de.whs.wi.friends_and_places.error.ResourceNotFoundException if no data is found for the given zip code
     */
    GeocodingData getGeoDataFromZipCode(String zipCode);

    /**
     * Retrieves geographic data for a specific street address
     *
     * @param street The street name
     * @param housenumber The house or building number
     * @param city The city or locality
     * @param country The country
     * @return GeocodingData containing detailed location information for the address
     * @throws de.whs.wi.friends_and_places.error.ResourceNotFoundException if no data is found for the given address
     */
    GeocodingData getGeoDataFromAddress(String street, String housenumber, String city, String country);

    /**
     * Performs reverse geocoding to retrieve address information for specific coordinates
     *
     * @param latitude The latitude coordinate
     * @param longitude The longitude coordinate
     * @return GeocodingData containing address information for the given coordinates
     * @throws de.whs.wi.friends_and_places.error.ResourceNotFoundException if no data is found for the given coordinates
     */
    GeocodingData getReverseGeoData(double latitude, double longitude);
}
