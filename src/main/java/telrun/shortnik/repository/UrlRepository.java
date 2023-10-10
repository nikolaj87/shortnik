package telrun.shortnik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import telrun.shortnik.entity.Url;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> findByShortUrl(String shortUrl);

    Optional<Url> findByLongUrl(String longUrl);

    void deleteUrlByShortUrl(String shortUrl);

    List<Url> findUrlByLastUseBefore(Timestamp timeCheck);
}
