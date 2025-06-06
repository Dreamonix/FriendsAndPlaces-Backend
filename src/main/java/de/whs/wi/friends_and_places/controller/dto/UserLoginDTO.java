package de.whs.wi.friends_and_places.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data transfer object for user login")
public class UserLoginDTO {
    @Schema(description = "User's email address", example = "john.doe@example.com", required = true)
    String email;

    @Schema(description = "User's password", example = "securePassword123", required = true)
    String password;

    public UserLoginDTO() {
    }

    public UserLoginDTO(String email, String password) {
        this.email = email;
        this.password = password;
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
}
