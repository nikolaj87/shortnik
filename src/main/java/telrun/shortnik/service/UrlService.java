package telrun.shortnik.service;

import org.springframework.transaction.annotation.Transactional;
import telrun.shortnik.dto.UrlRequest;

public interface UrlService {
    @Transactional
    String createUrl(UrlRequest urlRequest);
    @Transactional(readOnly = true)
    String getLongUrlByShortName(String urlShort);
    @Transactional
    void deleteUrl(String shortUrl);
}
