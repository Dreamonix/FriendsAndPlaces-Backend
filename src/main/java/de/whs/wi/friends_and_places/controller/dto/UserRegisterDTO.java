package de.whs.wi.friends_and_places.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data transfer object for user registration")
public class UserRegisterDTO {
    @Schema(description = "Username for the new account", example = "johndoe", required = true)
    private String username;

    @Schema(description = "Email address", example = "john.doe@example.com", required = true)
    private String email;

    @Schema(description = "Password for the account", example = "securePassword123", required = true)
    private String password;

    @Schema(description = "City of residence", example = "Berlin")
    private String city;

    @Schema(description = "Postal/ZIP code", example = "10115")
    private String zipCode;

    @Schema(description = "Street name", example = "Friedrichstraße")
    private String street;

    @Schema(description = "House number", example = "123")
    private String houseNumber;

    @Schema(description = "Mobile phone number", example = "+49123456789")
    private String mobile;

    public UserRegisterDTO() {
    }

    public UserRegisterDTO(String username, String email, String password, String city, String zipCode, String street, String houseNumber, String mobile) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.city = city;
        this.zipCode = zipCode;
        this.street = street;
        this.houseNumber = houseNumber;
        this.mobile = mobile;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
