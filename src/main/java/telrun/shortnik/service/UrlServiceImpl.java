package telrun.shortnik.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telrun.shortnik.service.cache.CacheManager;
import telrun.shortnik.dto.UrlRequest;

@Service
public class UrlServiceImpl implements UrlService {
    private final CacheManager cacheManager;

    @Autowired
    public UrlServiceImpl(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public String createUrl(UrlRequest urlRequest) {
        return cacheManager.writeData(urlRequest);
    }

    @Override
    public String getLongUrlByShortName(String urlShort) {
        return cacheManager.readData(urlShort);
    }

    @Override
    public void deleteUrl(String shortUrl) {
        cacheManager.deleteData(shortUrl);

    }
}

