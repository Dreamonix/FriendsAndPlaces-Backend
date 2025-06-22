package de.whs.wi.friends_and_places.controller;

import de.whs.wi.friends_and_places.model.GeocodingData;
import de.whs.wi.friends_and_places.service.GeocodeApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for geocoding related operations
 */
@RestController
@RequestMapping("/api/v1/geocode")
@Tag(name = "Geocode", description = "API endpoints for geocoding operations")
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
    @Operation(summary = "Get geocoding data by zip code",
            description = "Retrieves geographic data based on the provided postal/zip code.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Geocoding data retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Zip code not found")
    })
    @GetMapping("/zip/{zipCode}")
    public ResponseEntity<GeocodingData> getLocationByZipCode(@PathVariable String zipCode) {
        logger.info("Request received for geocoding data of zip code: {}", zipCode);

        GeocodingData geocodingData = geocodeApiService.getGeoDataFromZipCode(zipCode);
        return ResponseEntity.ok(geocodingData);
    }

    /**
      * Get geographic data for a specific address
      *
      * @param street The street name
      * @param housenumber The house number
      * @param city The city name
      * @param country The country name
      * @return GeocodingData containing location information
      */
    @Operation(summary = "Get geocoding data by address",
            description = "Retrieves geographic data based on the provided address details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Geocoding data retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Address not found")
    })
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

    /**
     * Get geographic data for a specific set of coordinates
     *
     * @param latitude The latitude of the location
     * @param longitude The longitude of the location
     * @return GeocodingData containing location information
     */
    @Operation(summary = "Get geocoding data by coordinates",
            description = "Retrieves geographic data based on the provided latitude and longitude.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Geocoding data retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Coordinates not found")
    })
   @GetMapping("/coordinates")
    public ResponseEntity<GeocodingData> getLocationByCoordinates(
              @RequestParam double latitude,
              @RequestParam double longitude) {
         logger.info("Request received for geocoding data of coordinates: {}, {}", latitude, longitude);

         GeocodingData geocodingData = geocodeApiService.getReverseGeoData(latitude, longitude);
         return ResponseEntity.ok(geocodingData);
    }
}
