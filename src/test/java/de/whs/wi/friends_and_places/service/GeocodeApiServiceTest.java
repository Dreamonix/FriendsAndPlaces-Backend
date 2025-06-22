package de.whs.wi.friends_and_places.service;

import de.whs.wi.friends_and_places.config.ExternalApiConfig;
import de.whs.wi.friends_and_places.error.GeocodingValidationException;
import de.whs.wi.friends_and_places.error.ResourceNotFoundException;
import de.whs.wi.friends_and_places.model.GeocodingData;
import de.whs.wi.friends_and_places.service.implementations.GeocodeApiServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GeocodeApiServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ExternalApiConfig apiConfig;

    private GeocodeApiService geocodeApiService;

    @BeforeEach
    public void setup() {
        // These stubbings are only used in the API call tests, not in validation tests
        lenient().when(apiConfig.getBaseUrl()).thenReturn("https://api.example.com/v1");
        lenient().when(apiConfig.getApiKey()).thenReturn("test-api-key");

        geocodeApiService = new GeocodeApiServiceImpl(restTemplate, apiConfig);
    }

    @Test
    public void testGetGeoDataFromZipCode_Success() {
        // Arrange
        String zipCode = "46419";

        // Create mock response
        Map<String, Object> responseMap = createMockGeocodeResponse();

        @SuppressWarnings("unchecked")
        ResponseEntity<HashMap<String, Object>> responseEntity = new ResponseEntity<>(
                (HashMap<String, Object>) responseMap, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                ArgumentMatchers.<ParameterizedTypeReference<HashMap<String, Object>>>any())
        ).thenReturn(responseEntity);

        // Act
        GeocodingData result = geocodeApiService.getGeoDataFromZipCode(zipCode);

        // Assert
        assertNotNull(result);
        assertEquals("Germany", result.getCountry());
        assertEquals("North Rhine-Westphalia", result.getState());
        assertEquals("Isselburg", result.getCity());
        assertEquals("46419", result.getPostcode());
        assertEquals(6.456254616, result.getLon());
        assertEquals(51.83433668, result.getLat());
    }


    @Test
    public void testGetGeoDataFromAddress_Success() {
        // Arrange
        String street = "Usambaraweg";
        String housenumber = "18";
        String city = "Isselburg";
        String country = "Germany";

        // Create mock response
        Map<String, Object> responseMap = createMockGeocodeResponse();

        @SuppressWarnings("unchecked")
        ResponseEntity<HashMap<String, Object>> responseEntity = new ResponseEntity<>(
                (HashMap<String, Object>) responseMap, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                ArgumentMatchers.<ParameterizedTypeReference<HashMap<String, Object>>>any())
        ).thenReturn(responseEntity);

        // Act
        GeocodingData result = geocodeApiService.getGeoDataFromAddress(street, housenumber, city, country);

        // Assert
        assertNotNull(result);
        assertEquals("Germany", result.getCountry());
        assertEquals("Isselburg", result.getCity());
        assertEquals("Usambaraweg", result.getStreet());
        assertEquals("18", result.getHousenumber());
    }

    @Test
    public void testGetReverseGeoData_Success() {
        // Arrange
        double latitude = 51.83433668;
        double longitude = 6.456254616;

        // Create mock response
        Map<String, Object> responseMap = createMockGeocodeResponse();

        @SuppressWarnings("unchecked")
        ResponseEntity<HashMap<String, Object>> responseEntity = new ResponseEntity<>(
                (HashMap<String, Object>) responseMap, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                ArgumentMatchers.<ParameterizedTypeReference<HashMap<String, Object>>>any())
        ).thenReturn(responseEntity);

        // Act
        GeocodingData result = geocodeApiService.getReverseGeoData(latitude, longitude);

        // Assert
        assertNotNull(result);
        assertEquals("Germany", result.getCountry());
        assertEquals("Isselburg", result.getCity());
        assertEquals(6.456254616, result.getLon());
        assertEquals(51.83433668, result.getLat());
    }

    @Test
    public void testGetReverseGeoData_InvalidLatitude() {
        // Arrange
        double latitude = 100.0; // Invalid: > 90
        double longitude = 6.456254616;

        // Act & Assert
        assertThrows(GeocodingValidationException.class, () -> {
            geocodeApiService.getReverseGeoData(latitude, longitude);
        });
    }

    @Test
    public void testGetReverseGeoData_InvalidLongitude() {
        // Arrange
        double latitude = 51.83433668;
        double longitude = 200.0; // Invalid: > 180

        // Act & Assert
        assertThrows(GeocodingValidationException.class, () -> {
            geocodeApiService.getReverseGeoData(latitude, longitude);
        });
    }

    /**
     * Creates a mock response similar to what Geoapify would return
     */
    private Map<String, Object> createMockGeocodeResponse() {
        Map<String, Object> responseMap = new HashMap<>();
        List<Map<String, Object>> results = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();

        // Basic location data
        result.put("country", "Germany");
        result.put("country_code", "de");
        result.put("state", "North Rhine-Westphalia");
        result.put("county", "Kreis Borken");
        result.put("city", "Isselburg");
        result.put("postcode", "46419");
        result.put("street", "Usambaraweg");
        result.put("housenumber", "18");
        result.put("lon", 6.456254616);
        result.put("lat", 51.83433668);
        result.put("formatted", "Usambaraweg 18, 46419 Isselburg, Germany");
        result.put("address_line1", "Usambaraweg 18");
        result.put("address_line2", "46419 Isselburg, Germany");

        // Add datasource
        Map<String, Object> datasource = new HashMap<>();
        datasource.put("sourcename", "openstreetmap");
        datasource.put("attribution", "© OpenStreetMap contributors");
        datasource.put("license", "Open Database License");
        datasource.put("url", "https://www.openstreetmap.org/copyright");
        result.put("datasource", datasource);

        // Add timezone
        Map<String, Object> timezone = new HashMap<>();
        timezone.put("name", "Europe/Berlin");
        timezone.put("offset_STD", "+01:00");
        timezone.put("offset_STD_seconds", 3600);
        timezone.put("offset_DST", "+02:00");
        timezone.put("offset_DST_seconds", 7200);
        timezone.put("abbreviation_STD", "CET");
        timezone.put("abbreviation_DST", "CEST");
        result.put("timezone", timezone);

        // Add bbox
        Map<String, Object> bbox = new HashMap<>();
        bbox.put("lon1", 6.3868495);
        bbox.put("lat1", 51.802759);
        bbox.put("lon2", 6.5323314);
        bbox.put("lat2", 51.8740311);
        result.put("bbox", bbox);

        // Add rank
        Map<String, Object> rank = new HashMap<>();
        rank.put("popularity", 4.845203977106095);
        rank.put("confidence", 1.0);
        rank.put("confidence_city_level", 1.0);
        rank.put("match_type", "full_match");
        result.put("rank", rank);

        // Add result to results list
        results.add(result);

        // Add results to response
        responseMap.put("results", results);

        return responseMap;
    }
}
