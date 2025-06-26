package de.whs.wi.friends_and_places.service.implementations;

import de.whs.wi.friends_and_places.error.ResourceNotFoundException;
import de.whs.wi.friends_and_places.model.FriendRequest;
import de.whs.wi.friends_and_places.model.User;
import de.whs.wi.friends_and_places.repository.FriendRequestRepository;
import de.whs.wi.friends_and_places.repository.UserRepository;
import de.whs.wi.friends_and_places.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import de.whs.wi.friends_and_places.controller.dto.FriendRequestStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FriendServiceImpl implements FriendService {

    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;

    @Autowired
    public FriendServiceImpl(FriendRequestRepository friendRequestRepository, UserRepository userRepository) {
        this.friendRequestRepository = friendRequestRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public FriendRequest sendFriendRequest(User sender, Long receiverId) {
        // Find the receiver
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", receiverId));

        // Validate that sender is not trying to friend themselves
        if (sender.getId().equals(receiverId)) {
            throw new IllegalArgumentException("You cannot send a friend request to yourself");
        }

        // Check if users are already friends
        if (sender.isFriend(receiver)) {
            throw new IllegalStateException("You are already friends with this user");
        }

        // Check if a pending request already exists
        if (friendRequestRepository.existsBySenderAndReceiverAndStatus(
                sender, receiver, FriendRequestStatus.PENDING)) {
            throw new IllegalStateException("A pending friend request already exists");
        }

        // Check if the receiver has already sent a request to the sender
        if (friendRequestRepository.existsBySenderAndReceiverAndStatus(
                receiver, sender, FriendRequestStatus.PENDING)) {
            throw new IllegalStateException("This user has already sent you a friend request");
        }

        // Create and save the friend request
        FriendRequest friendRequest = new FriendRequest(sender, receiver);
        return friendRequestRepository.save(friendRequest);
    }

    @Override
    @Transactional
    public FriendRequest acceptFriendRequest(Long requestId, User user) {
        FriendRequest request = getFriendRequestAndValidateReceiver(requestId, user);

        // Update request status
        request.accept();

        // Add the friendship relationship
        user.addFriend(request.getSender());

        // Save the updated request and user relationships
        userRepository.save(user);
        userRepository.save(request.getSender());
        return friendRequestRepository.save(request);
    }

    @Override
    @Transactional
    public FriendRequest declineFriendRequest(Long requestId, User user) {
        FriendRequest request = getFriendRequestAndValidateReceiver(requestId, user);

        // Update request status
        request.decline();

        // Save the updated request
        return friendRequestRepository.save(request);
    }

    @Override
    @Transactional
    public FriendRequest cancelFriendRequest(Long requestId, User user) {
        // Find the request
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("FriendRequest", "id", requestId));

        // Verify the user is the sender
        if (!request.getSender().getId().equals(user.getId())) {
            throw new IllegalStateException("Only the sender can cancel a friend request");
        }

        // Verify the request is still pending
        if (!request.isPending()) {
            throw new IllegalStateException("Only pending requests can be canceled");
        }

        // Update request status
        request.cancel();

        // Save the updated request
        return friendRequestRepository.save(request);
    }

    @Override
    public List<FriendRequest> getSentPendingRequests(User user) {
        return friendRequestRepository.findBySenderAndStatus(user, FriendRequestStatus.PENDING);
    }

    @Override
    public List<FriendRequest> getReceivedPendingRequests(User user) {
        return friendRequestRepository.findByReceiverAndStatus(user, FriendRequestStatus.PENDING);
    }

    @Override
    public List<User> getFriends(User user) {
        return new ArrayList<>(user.getFriends());
    }

    @Override
    @Transactional
    public void removeFriend(User user, Long friendId) {
        // Find the friend
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", friendId));

        // Verify they are friends
        if (!user.isFriend(friend)) {
            throw new IllegalStateException("You are not friends with this user");
        }

        // Remove the friendship relationship
        user.removeFriend(friend);

        // Save the updated user relationships
        userRepository.save(user);
        userRepository.save(friend);
    }

    /**
     * Helper method to find a friend request and validate that the given user is the receiver.
     */
    private FriendRequest getFriendRequestAndValidateReceiver(Long requestId, User user) {
        // Find the request
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("FriendRequest", "id", requestId));

        // Verify the user is the receiver
        if (!request.getReceiver().getId().equals(user.getId())) {
            throw new IllegalStateException("Only the receiver can accept or decline a friend request");
        }

        // Verify the request is still pending
        if (!request.isPending()) {
            throw new IllegalStateException("Only pending requests can be accepted or declined");
        }

        return request;
    }
}
