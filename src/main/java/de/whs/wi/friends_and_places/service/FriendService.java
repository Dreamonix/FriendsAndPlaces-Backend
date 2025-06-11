package de.whs.wi.friends_and_places.service;

import de.whs.wi.friends_and_places.model.FriendRequest;
import de.whs.wi.friends_and_places.model.User;

import java.util.List;

/**
 * Service interface for managing friend requests.
 */
public interface FriendService {

    /**
     * Send a friend request from one user to another.
     *
     * @param sender the user sending the request
     * @param receiverId the ID of the user receiving the request
     * @return the created friend request
     * @throws IllegalArgumentException if the receiver doesn't exist or sender tries to friend themselves
     * @throws IllegalStateException if a pending request already exists or users are already friends
     */
    FriendRequest sendFriendRequest(User sender, Long receiverId);

    /**
     * Accept a friend request.
     *
     * @param requestId the ID of the friend request
     * @param user the user accepting the request (must be the receiver)
     * @return the accepted friend request
     * @throws IllegalArgumentException if the request doesn't exist
     * @throws IllegalStateException if the user is not the receiver or the request is not pending
     */
    FriendRequest acceptFriendRequest(Long requestId, User user);

    /**
     * Decline a friend request.
     *
     * @param requestId the ID of the friend request
     * @param user the user declining the request (must be the receiver)
     * @return the declined friend request
     * @throws IllegalArgumentException if the request doesn't exist
     * @throws IllegalStateException if the user is not the receiver or the request is not pending
     */
    FriendRequest declineFriendRequest(Long requestId, User user);

    /**
     * Cancel a friend request.
     *
     * @param requestId the ID of the friend request
     * @param user the user canceling the request (must be the sender)
     * @return the canceled friend request
     * @throws IllegalArgumentException if the request doesn't exist
     * @throws IllegalStateException if the user is not the sender or the request is not pending
     */
    FriendRequest cancelFriendRequest(Long requestId, User user);

    /**
     * Get all pending friend requests sent by a user.
     *
     * @param user the user
     * @return a list of pending friend requests sent by the user
     */
    List<FriendRequest> getSentPendingRequests(User user);

    /**
     * Get all pending friend requests received by a user.
     *
     * @param user the user
     * @return a list of pending friend requests received by the user
     */
    List<FriendRequest> getReceivedPendingRequests(User user);

    /**
     * Get all friends of a user.
     *
     * @param user the user
     * @return a list of the user's friends
     */
    List<User> getFriends(User user);

    /**
     * Remove a friend relationship.
     *
     * @param user the user removing a friend
     * @param friendId the ID of the friend to remove
     * @throws IllegalArgumentException if the friend doesn't exist
     * @throws IllegalStateException if the users are not friends
     */
    void removeFriend(User user, Long friendId);
}
