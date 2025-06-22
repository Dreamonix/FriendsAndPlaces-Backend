package de.whs.wi.friends_and_places.repository;

import de.whs.wi.friends_and_places.model.User;
import de.whs.wi.friends_and_places.model.UserLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for accessing UserLocation entities
 */
@Repository
public interface UserLocationRepository extends JpaRepository<UserLocation, Long> {

    /**
     * Find all locations for a specific user, ordered by creation date (newest first)
     *
     * @param user The user whose locations to find
     * @return List of user locations sorted by creation date descending
     */
    List<UserLocation> findByUserOrderByCreatedAtDesc(User user);

    /**
     * Find the most recent location for a specific user
     *
     * @param user The user whose location to find
     * @return Optional containing the most recent location, or empty if none exists
     */
    Optional<UserLocation> findFirstByUserOrderByCreatedAtDesc(User user);

    /**
     * Find the most recent location for each user in a list of users
     *
     * @param users The list of users whose latest locations to find
     * @return List of the most recent location for each user
     */
    @Query("SELECT ul FROM UserLocation ul WHERE ul.id IN " +
           "(SELECT MAX(ul2.id) FROM UserLocation ul2 WHERE ul2.user IN :users GROUP BY ul2.user)")
    List<UserLocation> findLatestLocationsByUsers(@Param("users") List<User> users);
}
