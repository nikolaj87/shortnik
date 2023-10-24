package telrun.shortnik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import telrun.shortnik.entity.Url;
import telrun.shortnik.entity.User;

import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> findByShortUrl(String shortUrl);
    void deleteUrlByShortUrl(String shortUrl);
    @Modifying
    @Query(value = "DELETE FROM url WHERE (user_id IN (SELECT id FROM user WHERE id NOT IN (SELECT user_id FROM role_user WHERE role_id = 2)) AND DATE_SUB(NOW(), INTERVAL :hours HOUR) > created_at);", nativeQuery = true)
    void deleteOutdatedUrls(@Param("hours") int hours);
    int countAllByUser (User user);
}
