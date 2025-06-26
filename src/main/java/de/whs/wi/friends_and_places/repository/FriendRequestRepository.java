package de.whs.wi.friends_and_places.repository;

import de.whs.wi.friends_and_places.model.FriendRequest;
import de.whs.wi.friends_and_places.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import de.whs.wi.friends_and_places.controller.dto.FriendRequestStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    /**
     * Find all friend requests received by a user with a specific status.
     *
     * @param receiver the user who received the requests
     * @param status the status of the requests
     * @return a list of friend requests
     */
    List<FriendRequest> findByReceiverAndStatus(User receiver, FriendRequestStatus status);

    /**
     * Find all friend requests sent by a user with a specific status.
     *
     * @param sender the user who sent the requests
     * @param status the status of the requests
     * @return a list of friend requests
     */
    List<FriendRequest> findBySenderAndStatus(User sender,FriendRequestStatus status);

    /**
     * Find pending friend request between two users.
     *
     * @param sender the user who sent the request
     * @param receiver the user who received the request
     * @return the friend request if it exists
     */
    Optional<FriendRequest> findBySenderAndReceiverAndStatus(
            User sender, User receiver,FriendRequestStatus status);

    /**
     * Check if a pending friend request exists between two users.
     *
     * @param sender the user who sent the request
     * @param receiver the user who received the request
     * @return true if a pending request exists, false otherwise
     */
    boolean existsBySenderAndReceiverAndStatus(
            User sender, User receiver, FriendRequestStatus status);

    /**
     * Find all pending friend requests for a user (both sent and received).
     *
     * @param user the user
     * @return a list of pending friend requests
     */
    @Query("SELECT fr FROM FriendRequest fr WHERE " +
           "(fr.sender = :user OR fr.receiver = :user) AND fr.status = 'PENDING'")
    List<FriendRequest> findAllPendingRequestsForUser(@Param("user") User user);
}
