package telrun.shortnik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import telrun.shortnik.entity.Url;
import telrun.shortnik.entity.User;

import java.util.Optional;
/**
 * Repository interface for Url entities, extending JpaRepository.
 */
public interface UrlRepository extends JpaRepository<Url, Long> {
    /**
     * Find a URL by its short URL.
     *
     * @param shortUrl The short URL to search for.
     * @return An optional containing the URL with the specified short URL if found.
     */
    Optional<Url> findByShortUrl(String shortUrl);
    /**
     * Delete a URL by its short URL.
     *
     * @param shortUrl The short URL of the URL to be deleted.
     */
    void deleteUrlByShortUrl(String shortUrl);
    /**
     * Delete outdated URLs based on a specified time interval.
     *
     * @param hours The number of hours to consider for URL deletion.
     */
    @Modifying
    @Query(value = "DELETE FROM url WHERE (user_id IN (SELECT id FROM user WHERE id NOT IN (SELECT user_id FROM role_user WHERE role_id = 2)) AND DATE_SUB(NOW(), INTERVAL :hours HOUR) > created_at);", nativeQuery = true)
    void deleteOutdatedUrls(@Param("hours") int hours);
    /**
     * Count the total number of URLs associated with a specific user.
     *
     * @param user The user for which the URL count is requested.
     * @return The total count of URLs associated with the specified user.
     */
    int countAllByUser (User user);
}
