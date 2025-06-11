package de.whs.wi.friends_and_places.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Data Transfer Object for users.
 * Used for sending simplified user data in API responses.
 */
@Schema(description = "User information")
public class UserDTO {

    @Schema(description = "Unique identifier of the user", example = "1")
    private Long id;

    @Schema(description = "Username of the user", example = "johndoe")
    private String username;

    @Schema(description = "Email address of the user", example = "john.doe@example.com")
    private String email;

    // Constructors
    public UserDTO() {
    }

    public UserDTO(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
}
