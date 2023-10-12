package telrun.shortnik.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telrun.shortnik.service.cache.UrlReadCacheManager;
import telrun.shortnik.dto.UrlRequest;

@Service
public class UrlServiceImpl implements UrlService {
    private final UrlReadCacheManager urlReadCacheManager;

    @Autowired
    public UrlServiceImpl(UrlReadCacheManager urlReadCacheManager) {
        this.urlReadCacheManager = urlReadCacheManager;
    }

    @Override
    public String createUrl(UrlRequest urlRequest) {
        return urlReadCacheManager.writeData(urlRequest);
    }

    @Override
    public String getLongUrlByShortName(String urlShort) {
        return urlReadCacheManager.readData(urlShort);
    }

    @Override
    public void deleteUrl(String shortUrl) {
        urlReadCacheManager.deleteData(shortUrl);
    }
}

