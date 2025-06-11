package de.whs.wi.friends_and_places.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for friend requests.
 * Used for sending friend request data in API responses.
 */
@Schema(description = "Friend request information")
public class FriendRequestDTO {

    @Schema(description = "Unique identifier of the request", example = "1")
    private Long id;

    @Schema(description = "User who sent the request")
    private UserDTO sender;

    @Schema(description = "User who received the request")
    private UserDTO receiver;

    @Schema(description = "When the request was sent", example = "2025-06-06T12:30:45")
    private LocalDateTime requestTime;

    @Schema(description = "When the request was responded to (accepted/declined)", example = "2025-06-06T14:15:22")
    private LocalDateTime responseTime;

    @Schema(description = "Current status of the request", example = "PENDING",
            allowableValues = {"PENDING", "ACCEPTED", "DECLINED", "CANCELED"})
    private String status;

    // Constructors
    public FriendRequestDTO() {
    }

    public FriendRequestDTO(Long id, UserDTO sender, UserDTO receiver,
                            LocalDateTime requestTime, LocalDateTime responseTime, String status) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.requestTime = requestTime;
        this.responseTime = responseTime;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getSender() {
        return sender;
    }

    public void setSender(UserDTO sender) {
        this.sender = sender;
    }

    public UserDTO getReceiver() {
        return receiver;
    }

    public void setReceiver(UserDTO receiver) {
        this.receiver = receiver;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(LocalDateTime requestTime) {
        this.requestTime = requestTime;
    }

    public LocalDateTime getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(LocalDateTime responseTime) {
        this.responseTime = responseTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
