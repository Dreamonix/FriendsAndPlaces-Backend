package de.whs.wi.friends_and_places.controller.dto;

public enum FriendRequestStatus {
    PENDING,    // Request has been sent but not yet accepted or declined
    ACCEPTED,   // Request has been accepted, users are now friends
    DECLINED,   // Request has been declined
    CANCELED    // Request was canceled by the sender
}
