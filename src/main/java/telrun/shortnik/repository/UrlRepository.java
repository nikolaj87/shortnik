package telrun.shortnik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import telrun.shortnik.entity.Url;

import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> findByShortUrl(String shortUrl);

    Optional<Url> findByLongUrl(String longUrl);
        void deleteUrlByShortUrl(String shortUrl);

}
