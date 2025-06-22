package de.whs.wi.friends_and_places.service;

import de.whs.wi.friends_and_places.model.GeocodingData;

/**
 * Service interface for external geocoding API operations
 */
public interface GeocodeApiService {

    /**
     * Get geographic data for a specific zip code
     *
     * @param zipCode The zip/postal code to get geographic data for
     * @return GeocodingData containing geographic information from Geoapify
     */
    GeocodingData getGeoDataFromZipCode(String zipCode);

    GeocodingData getGeoDataFromAddress(String street,String housenumber, String city, String country);
}
