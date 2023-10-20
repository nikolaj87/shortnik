package telrun.shortnik.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import telrun.shortnik.dto.UrlCache;
import telrun.shortnik.dto.UrlRequest;
import telrun.shortnik.entity.User;

import java.util.List;

public interface UrlService {
    @Transactional
    String createUrl(UrlRequest urlRequest, User user);
    @Transactional
    String getLongUrlByShortName(String urlShort);
    @Transactional
    void deleteUrl(String shortUrl);
    @Transactional
    void cleanDatabase();

    public void updateUrl(List<UrlCache> listForUpdate);
}
