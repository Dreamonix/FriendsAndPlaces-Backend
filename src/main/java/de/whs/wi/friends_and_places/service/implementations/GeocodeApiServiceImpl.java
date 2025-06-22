package de.whs.wi.friends_and_places.service.implementations;

import de.whs.wi.friends_and_places.config.ExternalApiConfig;
import de.whs.wi.friends_and_places.error.ResourceNotFoundException;
import de.whs.wi.friends_and_places.model.GeocodingData;
import de.whs.wi.friends_and_places.service.GeocodeApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

/**
 * Implementation of the GeocodeApiService that makes HTTP requests to external geocoding APIs
 */
@Service
public class GeocodeApiServiceImpl implements GeocodeApiService {

    private static final Logger logger = LoggerFactory.getLogger(GeocodeApiServiceImpl.class);

    private final RestTemplate restTemplate;
    private final ExternalApiConfig apiConfig;

    public GeocodeApiServiceImpl(RestTemplate restTemplate, ExternalApiConfig apiConfig) {
        this.restTemplate = restTemplate;
        this.apiConfig = apiConfig;
    }

    @Override
    public GeocodingData getGeoDataFromZipCode(String zipCode) {
        logger.info("Fetching geo data for zip code: {}", zipCode);

        try {
            // Create URL with parameters
            String url = UriComponentsBuilder.fromHttpUrl(apiConfig.getBaseUrl() + "/geocode/search")
                    .queryParam("postcode", zipCode)
                    .queryParam("lang", "en")
                    .queryParam("limit", "1")
                    .queryParam("type", "postcode")
                    .queryParam("format", "json")
                    .queryParam("apiKey", apiConfig.getApiKey())
                    .build()
                    .toUriString();

            // For Geoapify, the API key is passed as a query parameter, not in headers
            HttpEntity<Void> requestEntity = new HttpEntity<>(new HttpHeaders());

            // Make the API call using a parameterized type to avoid unchecked assignment warning
            ResponseEntity<java.util.HashMap<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    new org.springframework.core.ParameterizedTypeReference<java.util.HashMap<String, Object>>() {}
            );

            if (response.getBody() == null) {
                throw new ResourceNotFoundException("No geo data found for zip code: " + zipCode);
            }

            // Log the response to see its structure
            logger.debug("API Response: {}", response.getBody());

            // Extract geocoding data from response
            return extractGeocodingDataFromResponse(response.getBody(), "zip code: " + zipCode);

        } catch (HttpClientErrorException.NotFound e) {
            logger.error("Geo data not found for zip code: {}", zipCode, e);
            throw new ResourceNotFoundException("No geo data found for zip code: " + zipCode);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("Error calling geocode API: {}", e.getMessage(), e);
            throw new RuntimeException("Error fetching geo data: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error when calling geocode API", e);
            throw new RuntimeException("Unexpected error fetching geo data", e);
        }
    }

    @Override
    public GeocodingData getGeoDataFromAddress(String street,String housenumber, String city, String country){
        logger.info("Fetching geo data for address: {}, {}, {}, {}", street,housenumber, city, country);

        try {
            // Create URL with parameters
            String url = UriComponentsBuilder.fromHttpUrl(apiConfig.getBaseUrl() + "/geocode/search")
                    .queryParam("street", street)
                    .queryParam("housenumber",housenumber)
                    .queryParam("city", city)
                    .queryParam("country", country)
                    .queryParam("type", "amenity")
                    .queryParam("lang", "en")
                    .queryParam("limit", "5")
                    .queryParam("format", "json")
                    .queryParam("apiKey", apiConfig.getApiKey())
                    .build()
                    .toUriString();

            // For Geoapify, the API key is passed as a query parameter, not in headers
            HttpEntity<Void> requestEntity = new HttpEntity<>(new HttpHeaders());

            // Make the API call using a parameterized type to avoid unchecked assignment warning
            ResponseEntity<java.util.HashMap<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    new org.springframework.core.ParameterizedTypeReference<java.util.HashMap<String, Object>>() {}
            );

            if (response.getBody() == null) {
                throw new ResourceNotFoundException("No geo data found for address: " + street + ", " + city + ", " + country);
            }

            // Log the response to see its structure
            logger.debug("API Response: {}", response.getBody());

            // Extract geocoding data from response
            return extractGeocodingDataFromResponse(response.getBody(), "address: " + street + ", " + city + ", " + country);

        } catch (HttpClientErrorException.NotFound e) {
            logger.error("Geo data not found for address: {}, {}, {}", street, city, country, e);
            throw new ResourceNotFoundException("No geo data found for address: " + street + ", " + city + ", " + country);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("Error calling geocode API: {}", e.getMessage(), e);
            throw new RuntimeException("Error fetching geo data: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error when calling geocode API", e);
            throw new RuntimeException("Unexpected error fetching geo data", e);
        }
    }

    /**
     * Extracts and maps geocoding data from the Geoapify API response
     *
     * @param responseBody The response body from the Geoapify API
     * @param locationDescription Description of the location for error messages
     * @return GeocodingData object populated with data from the response
     * @throws ResourceNotFoundException if the response doesn't contain valid geocoding data
     */
    private GeocodingData extractGeocodingDataFromResponse(Map<String, Object> responseBody, String locationDescription) {
        // The Geoapify response contains a wrapper with a "results" array
        if (responseBody.containsKey("results")) {
            Object resultsObj = responseBody.get("results");

            // Type-safe check and casting
            if (resultsObj instanceof List<?>) {
                List<?> resultsList = (List<?>) resultsObj;

                if (!resultsList.isEmpty() && resultsList.get(0) instanceof Map) {
                    // Safe to cast now that we've verified the structure
                    @SuppressWarnings("unchecked")
                    Map<String, Object> firstResult = (Map<String, Object>) resultsList.get(0);

                    // Create a new GeocodingData object and populate it manually
                    GeocodingData geocodingData = new GeocodingData();

                    // Populate the basic fields from the map
                    if (firstResult.containsKey("country")) geocodingData.setCountry((String) firstResult.get("country"));
                    if (firstResult.containsKey("country_code")) geocodingData.setCountryCode((String) firstResult.get("country_code"));
                    if (firstResult.containsKey("state")) geocodingData.setState((String) firstResult.get("state"));
                    if (firstResult.containsKey("county")) geocodingData.setCounty((String) firstResult.get("county"));
                    if (firstResult.containsKey("county_code")) geocodingData.setCountyCode((String) firstResult.get("county_code"));
                    if (firstResult.containsKey("city")) geocodingData.setCity((String) firstResult.get("city"));
                    if (firstResult.containsKey("district")) geocodingData.setDistrict((String) firstResult.get("district"));
                    if (firstResult.containsKey("street")) geocodingData.setStreet((String) firstResult.get("street"));
                    if (firstResult.containsKey("housenumber")) geocodingData.setHousenumber((String) firstResult.get("housenumber"));
                    if (firstResult.containsKey("postcode")) geocodingData.setPostcode((String) firstResult.get("postcode"));
                    if (firstResult.containsKey("formatted")) geocodingData.setFormatted((String) firstResult.get("formatted"));
                    if (firstResult.containsKey("address_line1")) geocodingData.setAddressLine1((String) firstResult.get("address_line1"));
                    if (firstResult.containsKey("address_line2")) geocodingData.setAddressLine2((String) firstResult.get("address_line2"));
                    if (firstResult.containsKey("lon")) geocodingData.setLon(Double.parseDouble(firstResult.get("lon").toString()));
                    if (firstResult.containsKey("lat")) geocodingData.setLat(Double.parseDouble(firstResult.get("lat").toString()));
                    if (firstResult.containsKey("category")) geocodingData.setCategory((String) firstResult.get("category"));
                    if (firstResult.containsKey("iso3166_2")) geocodingData.setIso31662((String) firstResult.get("iso3166_2"));
                    if (firstResult.containsKey("state_code")) geocodingData.setStateCode((String) firstResult.get("state_code"));
                    if (firstResult.containsKey("result_type")) geocodingData.setResultType((String) firstResult.get("result_type"));
                    if (firstResult.containsKey("plus_code")) geocodingData.setPlusCode((String) firstResult.get("plus_code"));
                    if (firstResult.containsKey("plus_code_short")) geocodingData.setPlusCodeShort((String) firstResult.get("plus_code_short"));
                    if (firstResult.containsKey("place_id")) geocodingData.setPlaceId((String) firstResult.get("place_id"));

                    // Handle nested objects: datasource
                    if (firstResult.containsKey("datasource") && firstResult.get("datasource") instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> datasourceMap = (Map<String, Object>) firstResult.get("datasource");
                        GeocodingData.Datasource datasource = new GeocodingData.Datasource();

                        if (datasourceMap.containsKey("sourcename")) datasource.setSourcename((String) datasourceMap.get("sourcename"));
                        if (datasourceMap.containsKey("attribution")) datasource.setAttribution((String) datasourceMap.get("attribution"));
                        if (datasourceMap.containsKey("license")) datasource.setLicense((String) datasourceMap.get("license"));
                        if (datasourceMap.containsKey("url")) datasource.setUrl((String) datasourceMap.get("url"));

                        geocodingData.setDatasource(datasource);
                    }

                    // Handle nested objects: timezone
                    if (firstResult.containsKey("timezone") && firstResult.get("timezone") instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> timezoneMap = (Map<String, Object>) firstResult.get("timezone");
                        GeocodingData.Timezone timezone = new GeocodingData.Timezone();

                        if (timezoneMap.containsKey("name")) timezone.setName((String) timezoneMap.get("name"));
                        if (timezoneMap.containsKey("offset_STD")) timezone.setOffsetSTD((String) timezoneMap.get("offset_STD"));
                        if (timezoneMap.containsKey("offset_STD_seconds")) {
                            Object seconds = timezoneMap.get("offset_STD_seconds");
                            if (seconds instanceof Number) {
                                timezone.setOffsetSTDSeconds(((Number) seconds).intValue());
                            }
                        }
                        if (timezoneMap.containsKey("offset_DST")) timezone.setOffsetDST((String) timezoneMap.get("offset_DST"));
                        if (timezoneMap.containsKey("offset_DST_seconds")) {
                            Object seconds = timezoneMap.get("offset_DST_seconds");
                            if (seconds instanceof Number) {
                                timezone.setOffsetDSTSeconds(((Number) seconds).intValue());
                            }
                        }
                        if (timezoneMap.containsKey("abbreviation_STD")) timezone.setAbbreviationSTD((String) timezoneMap.get("abbreviation_STD"));
                        if (timezoneMap.containsKey("abbreviation_DST")) timezone.setAbbreviationDST((String) timezoneMap.get("abbreviation_DST"));

                        geocodingData.setTimezone(timezone);
                    }

                    // Handle nested objects: rank
                    if (firstResult.containsKey("rank") && firstResult.get("rank") instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> rankMap = (Map<String, Object>) firstResult.get("rank");
                        GeocodingData.Rank rank = new GeocodingData.Rank();

                        if (rankMap.containsKey("popularity")) {
                            Object popularity = rankMap.get("popularity");
                            if (popularity instanceof Number) {
                                rank.setPopularity(((Number) popularity).doubleValue());
                            }
                        }
                        if (rankMap.containsKey("confidence")) {
                            Object confidence = rankMap.get("confidence");
                            if (confidence instanceof Number) {
                                rank.setConfidence(((Number) confidence).doubleValue());
                            }
                        }
                        if (rankMap.containsKey("confidence_city_level")) {
                            Object confidenceCityLevel = rankMap.get("confidence_city_level");
                            if (confidenceCityLevel instanceof Number) {
                                rank.setConfidenceCityLevel(((Number) confidenceCityLevel).doubleValue());
                            }
                        }
                        if (rankMap.containsKey("match_type")) rank.setMatchType((String) rankMap.get("match_type"));

                        geocodingData.setRank(rank);
                    }

                    // Handle nested objects: bbox (bounding box)
                    if (firstResult.containsKey("bbox") && firstResult.get("bbox") instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> bboxMap = (Map<String, Object>) firstResult.get("bbox");
                        GeocodingData.BoundingBox bbox = new GeocodingData.BoundingBox();

                        if (bboxMap.containsKey("lon1")) {
                            Object lon1 = bboxMap.get("lon1");
                            if (lon1 instanceof Number) {
                                bbox.setLon1(((Number) lon1).doubleValue());
                            }
                        }
                        if (bboxMap.containsKey("lat1")) {
                            Object lat1 = bboxMap.get("lat1");
                            if (lat1 instanceof Number) {
                                bbox.setLat1(((Number) lat1).doubleValue());
                            }
                        }
                        if (bboxMap.containsKey("lon2")) {
                            Object lon2 = bboxMap.get("lon2");
                            if (lon2 instanceof Number) {
                                bbox.setLon2(((Number) lon2).doubleValue());
                            }
                        }
                        if (bboxMap.containsKey("lat2")) {
                            Object lat2 = bboxMap.get("lat2");
                            if (lat2 instanceof Number) {
                                bbox.setLat2(((Number) lat2).doubleValue());
                            }
                        }

                        geocodingData.setBbox(bbox);
                    }

                    return geocodingData;
                }
            }
        }

        // If we can't properly parse the response, throw an exception
        throw new ResourceNotFoundException("Could not parse geocoding data for " + locationDescription);
    }
}



