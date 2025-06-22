package de.whs.wi.friends_and_places.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model class for Geoapify geocoding API response data
 */
public class GeocodingData {

    private String country;

    @JsonProperty("country_code")
    private String countryCode;

    private String state;

    private String county;

    @JsonProperty("county_code")
    private String countyCode;

    private String city;

    private String district;

    private String street;

    private String housenumber;

    private String postcode;

    @JsonProperty("iso3166_2")
    private String iso31662;

    private Datasource datasource;

    @JsonProperty("state_code")
    private String stateCode;

    @JsonProperty("result_type")
    private String resultType;

    private double lon;

    private double lat;

    private String formatted;

    @JsonProperty("address_line1")
    private String addressLine1;

    @JsonProperty("address_line2")
    private String addressLine2;

    private String category;

    private Timezone timezone;

    @JsonProperty("plus_code")
    private String plusCode;

    @JsonProperty("plus_code_short")
    private String plusCodeShort;

    private Rank rank;

    @JsonProperty("place_id")
    private String placeId;

    private BoundingBox bbox;

    // Inner classes for nested objects
    public static class Datasource {
        private String sourcename;
        private String attribution;
        private String license;
        private String url;

        // Getters and setters
        public String getSourcename() {
            return sourcename;
        }

        public void setSourcename(String sourcename) {
            this.sourcename = sourcename;
        }

        public String getAttribution() {
            return attribution;
        }

        public void setAttribution(String attribution) {
            this.attribution = attribution;
        }

        public String getLicense() {
            return license;
        }

        public void setLicense(String license) {
            this.license = license;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class Timezone {
        private String name;

        @JsonProperty("offset_STD")
        private String offsetSTD;

        @JsonProperty("offset_STD_seconds")
        private int offsetSTDSeconds;

        @JsonProperty("offset_DST")
        private String offsetDST;

        @JsonProperty("offset_DST_seconds")
        private int offsetDSTSeconds;

        @JsonProperty("abbreviation_STD")
        private String abbreviationSTD;

        @JsonProperty("abbreviation_DST")
        private String abbreviationDST;

        // Getters and setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOffsetSTD() {
            return offsetSTD;
        }

        public void setOffsetSTD(String offsetSTD) {
            this.offsetSTD = offsetSTD;
        }

        public int getOffsetSTDSeconds() {
            return offsetSTDSeconds;
        }

        public void setOffsetSTDSeconds(int offsetSTDSeconds) {
            this.offsetSTDSeconds = offsetSTDSeconds;
        }

        public String getOffsetDST() {
            return offsetDST;
        }

        public void setOffsetDST(String offsetDST) {
            this.offsetDST = offsetDST;
        }

        public int getOffsetDSTSeconds() {
            return offsetDSTSeconds;
        }

        public void setOffsetDSTSeconds(int offsetDSTSeconds) {
            this.offsetDSTSeconds = offsetDSTSeconds;
        }

        public String getAbbreviationSTD() {
            return abbreviationSTD;
        }

        public void setAbbreviationSTD(String abbreviationSTD) {
            this.abbreviationSTD = abbreviationSTD;
        }

        public String getAbbreviationDST() {
            return abbreviationDST;
        }

        public void setAbbreviationDST(String abbreviationDST) {
            this.abbreviationDST = abbreviationDST;
        }
    }

    public static class Rank {
        private double popularity;
        private double confidence;

        @JsonProperty("confidence_city_level")
        private double confidenceCityLevel;

        @JsonProperty("match_type")
        private String matchType;

        // Getters and setters
        public double getPopularity() {
            return popularity;
        }

        public void setPopularity(double popularity) {
            this.popularity = popularity;
        }

        public double getConfidence() {
            return confidence;
        }

        public void setConfidence(double confidence) {
            this.confidence = confidence;
        }

        public double getConfidenceCityLevel() {
            return confidenceCityLevel;
        }

        public void setConfidenceCityLevel(double confidenceCityLevel) {
            this.confidenceCityLevel = confidenceCityLevel;
        }

        public String getMatchType() {
            return matchType;
        }

        public void setMatchType(String matchType) {
            this.matchType = matchType;
        }
    }

    public static class BoundingBox {
        @JsonProperty("lon1")
        private double lon1;

        @JsonProperty("lat1")
        private double lat1;

        @JsonProperty("lon2")
        private double lon2;

        @JsonProperty("lat2")
        private double lat2;

        // Getters and setters
        public double getLon1() {
            return lon1;
        }

        public void setLon1(double lon1) {
            this.lon1 = lon1;
        }

        public double getLat1() {
            return lat1;
        }

        public void setLat1(double lat1) {
            this.lat1 = lat1;
        }

        public double getLon2() {
            return lon2;
        }

        public void setLon2(double lon2) {
            this.lon2 = lon2;
        }

        public double getLat2() {
            return lat2;
        }

        public void setLat2(double lat2) {
            this.lat2 = lat2;
        }
    }

    // Getters and setters for main class
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
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

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getIso31662() {
        return iso31662;
    }

    public void setIso31662(String iso31662) {
        this.iso31662 = iso31662;
    }

    public Datasource getDatasource() {
        return datasource;
    }

    public void setDatasource(Datasource datasource) {
        this.datasource = datasource;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getFormatted() {
        return formatted;
    }

    public void setFormatted(String formatted) {
        this.formatted = formatted;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Timezone getTimezone() {
        return timezone;
    }

    public void setTimezone(Timezone timezone) {
        this.timezone = timezone;
    }

    public String getPlusCode() {
        return plusCode;
    }

    public void setPlusCode(String plusCode) {
        this.plusCode = plusCode;
    }

    public String getPlusCodeShort() {
        return plusCodeShort;
    }

    public void setPlusCodeShort(String plusCodeShort) {
        this.plusCodeShort = plusCodeShort;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public BoundingBox getBbox() {
        return bbox;
    }

    public void setBbox(BoundingBox bbox) {
        this.bbox = bbox;
    }
}
