package de.whs.wi.friends_and_places.controller;

import de.whs.wi.friends_and_places.controller.dto.FriendRequestDTO;
import de.whs.wi.friends_and_places.controller.dto.UserDTO;
import de.whs.wi.friends_and_places.model.FriendRequest;
import de.whs.wi.friends_and_places.model.User;
import de.whs.wi.friends_and_places.service.FriendService;
import de.whs.wi.friends_and_places.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/friends")
@Tag(name = "Friends", description = "API endpoints for managing friends and friend requests")
public class FriendController {

    private final FriendService friendService;
    private final UserService userService;

    @Autowired
    public FriendController(FriendService friendService, UserService userService) {
        this.friendService = friendService;
        this.userService = userService;
    }

    @Operation(summary = "Send a friend request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friend request sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or receiver not found"),
            @ApiResponse(responseCode = "409", description = "Friend request already exists or users are already friends")
    })
    @PostMapping("/requests/{receiverId}")
    public ResponseEntity<FriendRequestDTO> sendFriendRequest(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long receiverId) {

        User sender = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        FriendRequest request = friendService.sendFriendRequest(sender, receiverId);
        return new ResponseEntity<>(convertToDTO(request), HttpStatus.OK);
    }

    @Operation(summary = "Accept a friend request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friend request accepted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or request not found"),
            @ApiResponse(responseCode = "403", description = "User is not the receiver of the request")
    })
    @PostMapping("/requests/{requestId}/accept")
    public ResponseEntity<FriendRequestDTO> acceptFriendRequest(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long requestId) {

        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        FriendRequest request = friendService.acceptFriendRequest(requestId, user);
        return new ResponseEntity<>(convertToDTO(request), HttpStatus.OK);
    }

    @Operation(summary = "Decline a friend request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friend request declined successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or request not found"),
            @ApiResponse(responseCode = "403", description = "User is not the receiver of the request")
    })
    @PostMapping("/requests/{requestId}/decline")
    public ResponseEntity<FriendRequestDTO> declineFriendRequest(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long requestId) {

        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        FriendRequest request = friendService.declineFriendRequest(requestId, user);
        return new ResponseEntity<>(convertToDTO(request), HttpStatus.OK);
    }

    @Operation(summary = "Cancel a friend request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friend request canceled successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or request not found"),
            @ApiResponse(responseCode = "403", description = "User is not the sender of the request")
    })
    @PostMapping("/requests/{requestId}/cancel")
    public ResponseEntity<FriendRequestDTO> cancelFriendRequest(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long requestId) {

        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        FriendRequest request = friendService.cancelFriendRequest(requestId, user);
        return new ResponseEntity<>(convertToDTO(request), HttpStatus.OK);
    }

    @Operation(summary = "Get all pending friend requests received by the current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of pending friend requests received")
    })
    @GetMapping("/requests/received")
    public ResponseEntity<List<FriendRequestDTO>> getReceivedFriendRequests(
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        List<FriendRequest> requests = friendService.getReceivedPendingRequests(user);
        List<FriendRequestDTO> requestDTOs = requests.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(requestDTOs, HttpStatus.OK);
    }

    @Operation(summary = "Get all pending friend requests sent by the current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of pending friend requests sent")
    })
    @GetMapping("/requests/sent")
    public ResponseEntity<List<FriendRequestDTO>> getSentFriendRequests(
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        List<FriendRequest> requests = friendService.getSentPendingRequests(user);
        List<FriendRequestDTO> requestDTOs = requests.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(requestDTOs, HttpStatus.OK);
    }

    @Operation(summary = "Get all friends of the current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of friends")
    })
    @GetMapping
    public ResponseEntity<List<UserDTO>> getFriends(
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        List<User> friends = friendService.getFriends(user);
        List<UserDTO> friendDTOs = friends.stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(friendDTOs, HttpStatus.OK);
    }

    @Operation(summary = "Remove a friend")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friend removed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or friend not found"),
            @ApiResponse(responseCode = "404", description = "Friend relationship does not exist")
    })
    @DeleteMapping("/{friendId}")
    public ResponseEntity<Void> removeFriend(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long friendId) {

        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        friendService.removeFriend(user, friendId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Get all available users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all users in the system")
    })
    @GetMapping("/available-users")
    public ResponseEntity<List<UserDTO>> getAllAvailableUsers(
            @AuthenticationPrincipal UserDetails userDetails) {

        // Get all users from the system
        List<User> allUsers = userService.findAllUsers();

        // Convert to DTOs
        List<UserDTO> userDTOs = allUsers.stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }

    /**
     * Convert a FriendRequest entity to a DTO for API responses.
     */
    private FriendRequestDTO convertToDTO(FriendRequest request) {
        return new FriendRequestDTO(
                request.getId(),
                convertToUserDTO(request.getSender()),
                convertToUserDTO(request.getReceiver()),
                request.getRequestTime(),
                request.getResponseTime(),
                request.getStatus().toString()
        );
    }

    /**
     * Convert a User entity to a simplified DTO for API responses.
     */
    private UserDTO convertToUserDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }
}
