package de.whs.wi.friends_and_places.service;

import de.whs.wi.friends_and_places.controller.dto.UserLoginDTO;
import de.whs.wi.friends_and_places.controller.dto.UserRegisterDTO;
import de.whs.wi.friends_and_places.model.User;

import java.util.Optional;

public interface UserService {
    /**
     * Finds a user by their ID.
     *
     * @param id the ID of the user
     * @return the User object if found, otherwise null
     */
    User findById(Long id);
    /**
     * Registers a new user with the provided details.
     *
     * @param userRegisterDTO the DTO containing user registration details
     * @return the registered User object
     * @throws IllegalArgumentException if any required field is missing or if the user already exists
     */
    User register(UserRegisterDTO userRegisterDTO);
    /**
     * Finds a user by their username.
     *
     * @param username the username of the user
     * @return an Optional containing the User if found, otherwise empty
     */
    Optional<User> findByUsername(String username);
    /**
     * Finds a user by their email.
     *
     * @param email the email of the user
     * @return an Optional containing the User if found, otherwise empty
     */
    Optional<User> findByEmail(String email);
    /**
     * Saves the user to the repository.
     *
     * @param user the User object to save
     * @return the saved User object
     */
    User save(User user);
    /**
     * Changes the password of the user.
     *
     * @param user the User object whose password is to be changed
     * @param newPassword the new password to set
     */
    void changePassword(User user, String newPassword);
    /**
     * Deletes the user from the repository.
     *
     * @param user the User object to delete
     */
    void deleteUser(User user);

    String authenticate(UserLoginDTO userLoginDTO);
}
