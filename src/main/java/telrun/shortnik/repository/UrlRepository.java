package telrun.shortnik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import telrun.shortnik.entity.Url;
import telrun.shortnik.entity.User;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> findByShortUrl(String shortUrl);
    void deleteUrlByShortUrl(String shortUrl);
    @Modifying
    @Query("UPDATE Url url SET url.lastUse = :timestamp WHERE url.id = :id")
    void updateUrlById(Long id, Timestamp timestamp);
    @Modifying
    @Query(value = "DELETE FROM url WHERE (user_id IN (SELECT id FROM user WHERE id NOT IN (SELECT user_id FROM role_user WHERE role_id = 2)) AND DATE_SUB(NOW(), INTERVAL 168 HOUR) > created_at) OR (DATE_SUB(NOW(), INTERVAL 30 DAY) > last_use);", nativeQuery = true)
    void deleteOutdatedUrls();
    int countAllByUser (User user);
}
