package telrun.shortnik.service;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.RedirectView;
import telrun.shortnik.dto.UrlRequest;

public interface UrlService {
    @Transactional
    ResponseEntity<String> createUrl(UrlRequest urlRequest);
    @Transactional(readOnly = true)
    ResponseEntity<String> getShortUrlByLongName(String url);
    @Transactional(readOnly = true)
    RedirectView getLongUrlByShorName(String urlShort);
    @Transactional
    void deleteUrl(String shortUrl);
}
