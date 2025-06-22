package de.whs.wi.friends_and_places.controller;

import de.whs.wi.friends_and_places.model.GeocodingData;
import de.whs.wi.friends_and_places.service.GeocodeApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for geocoding related operations
 */
@RestController
@RequestMapping("/api/v1/geocode")
public class GeocodeController {

    private static final Logger logger = LoggerFactory.getLogger(GeocodeController.class);

    private final GeocodeApiService geocodeApiService;

    public GeocodeController(GeocodeApiService geocodeApiService) {
        this.geocodeApiService = geocodeApiService;
    }

    /**
     * Get geographic data for a specific postal/zip code
     *
     * @param zipCode The postal/zip code to get data for
     * @return GeocodingData containing location information
     */
    @GetMapping("/zip/{zipCode}")
    public ResponseEntity<GeocodingData> getLocationByZipCode(@PathVariable String zipCode) {
        logger.info("Request received for geocoding data of zip code: {}", zipCode);

        GeocodingData geocodingData = geocodeApiService.getGeoDataFromZipCode(zipCode);
        return ResponseEntity.ok(geocodingData);
    }

   @GetMapping("/address")
   public ResponseEntity<GeocodingData> getLocationByAddress(
           @RequestParam String street,
           @RequestParam String housenumber,
           @RequestParam String city,
           @RequestParam String country) {
       logger.info("Request received for geocoding data of address: {}, {}, {}, {}", street ,housenumber, city, country);

       GeocodingData geocodingData = geocodeApiService.getGeoDataFromAddress(street,housenumber, city, country);
       return ResponseEntity.ok(geocodingData);
   }
}
