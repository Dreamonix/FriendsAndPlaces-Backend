package de.whs.wi.friends_and_places.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a friend request between users.
 * This tracks who sent the request, who received it, when it was sent,
 * when it was responded to, and the current status.
 */
@Entity
@Table(name = "friend_requests")
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Column(nullable = false)
    private LocalDateTime requestTime;

    @Column
    private LocalDateTime responseTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FriendRequestStatus status;

    /**
     * Enum representing the possible statuses of a friend request.
     */
    public enum FriendRequestStatus {
        PENDING,    // Request has been sent but not yet accepted or declined
        ACCEPTED,   // Request has been accepted, users are now friends
        DECLINED,   // Request has been declined
        CANCELED    // Request was canceled by the sender
    }

    // Constructors
    public FriendRequest() {
    }

    public FriendRequest(User sender, User receiver) {
        this.sender = sender;
        this.receiver = receiver;
        this.requestTime = LocalDateTime.now();
        this.status = FriendRequestStatus.PENDING;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
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

    public FriendRequestStatus getStatus() {
        return status;
    }

    public void setStatus(FriendRequestStatus status) {
        this.status = status;
    }

    /**
     * Accept this friend request.
     * Updates the status to ACCEPTED and sets the response time.
     */
    public void accept() {
        this.status = FriendRequestStatus.ACCEPTED;
        this.responseTime = LocalDateTime.now();
    }

    /**
     * Decline this friend request.
     * Updates the status to DECLINED and sets the response time.
     */
    public void decline() {
        this.status = FriendRequestStatus.DECLINED;
        this.responseTime = LocalDateTime.now();
    }

    /**
     * Cancel this friend request.
     * Updates the status to CANCELED and sets the response time.
     */
    public void cancel() {
        this.status = FriendRequestStatus.CANCELED;
        this.responseTime = LocalDateTime.now();
    }

    /**
     * Check if the request is still pending.
     * @return true if the request is pending, false otherwise
     */
    public boolean isPending() {
        return this.status == FriendRequestStatus.PENDING;
    }

    /**
     * Check if the request was accepted.
     * @return true if the request was accepted, false otherwise
     */
    public boolean isAccepted() {
        return this.status == FriendRequestStatus.ACCEPTED;
    }
}
