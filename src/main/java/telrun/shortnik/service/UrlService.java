package telrun.shortnik.service;

import org.springframework.transaction.annotation.Transactional;
import telrun.shortnik.dto.UrlRequest;
import telrun.shortnik.entity.User;

public interface UrlService {
    @Transactional
    String createUrl(UrlRequest urlRequest, User user);
    @Transactional
    String getLongUrlByShortName(String urlShort);
    @Transactional
    void deleteUrl(String shortUrl);
    @Transactional
    void cleanDatabase();
}
