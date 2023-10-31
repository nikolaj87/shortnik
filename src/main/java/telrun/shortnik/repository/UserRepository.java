package telrun.shortnik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telrun.shortnik.entity.User;

import java.util.Optional;
/**
 * Repository interface for User entities, extending JpaRepository.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Find a user by their name.
     *
     * @param name The name of the user to search for.
     * @return An optional containing the user with the specified name if found.
     */
    Optional<User> findUserByName(String name);
    /**
     * Delete a user by their name.
     *
     * @param name The name of the user to be deleted.
     */
    void deleteUserByName(String name);
}
