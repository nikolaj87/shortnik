package telrun.shortnik.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import telrun.shortnik.convertors.Convertors;
import telrun.shortnik.dto.UrlCache;
import telrun.shortnik.entity.Role;
import telrun.shortnik.entity.Url;
import telrun.shortnik.entity.User;
import telrun.shortnik.exception.NoPremiumRoleException;
import telrun.shortnik.generator.Generator;
import telrun.shortnik.repository.UrlRepository;
import telrun.shortnik.cache.UrlReadCacheManager;
import telrun.shortnik.dto.UrlRequest;

import java.sql.Timestamp;
import java.util.*;

@Service
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;
    private final UrlReadCacheManager urlCacheManager;
    private final Convertors convertors;
    private final Generator generator;
    private static final Logger logger = LoggerFactory.getLogger(UrlServiceImpl.class);
    private final static int MAX_URLS = 20;
    private final static long URL_LIFE_TIME = 604_800_000; //7days
    private static final Role PREMIUM_ROLE = new Role(2L, "ROLE_PREMIUM", null);

    @Autowired
    public UrlServiceImpl(UrlRepository urlRepository, UrlReadCacheManager urlCacheManager, Convertors convertors, Generator generator) {
        this.urlRepository = urlRepository;
        this.urlCacheManager = urlCacheManager;
        this.convertors = convertors;
        this.generator = generator;
    }

    @Override
    public String createUrl(UrlRequest urlRequest, User user) {
        if (!user.getRoles().contains(PREMIUM_ROLE)) {
            int quantityUrlsOfCurrentUser = urlRepository.countAllByUser(user);
            if (quantityUrlsOfCurrentUser >= MAX_URLS)
                throw new NoPremiumRoleException(MAX_URLS + " is the maximum number of links for a user. If you want more, please upgrade your account to premium.");
        }
        Url urlEntityForSave = convertors.requestToEntityUrl(urlRequest);
        urlEntityForSave.setShortUrl(generator.generate());
        urlRepository.save(urlEntityForSave);

        return urlEntityForSave.getShortUrl();
    }

    @Override
    public String getLongUrlByShortName(String urlShort) {

        UrlCache urlFromCache = urlCacheManager.findInCache(urlShort);
        if (urlFromCache != null) {
            urlFromCache.setLastUse(new Timestamp(System.currentTimeMillis()));
            return urlFromCache.getLongUrl();
        }
        Optional<Url> urlFromDatabaseOptional = urlRepository.findByShortUrl(urlShort);
        if (urlFromDatabaseOptional.isEmpty()) {
            return "/";
        }
        Url urlFromDatabase = urlFromDatabaseOptional.get();
        if (urlCacheManager.getCacheCurrentSize() < UrlReadCacheManager.getCacheMaxSize()) {
            urlCacheManager.putInCache(urlShort, new UrlCache(urlFromDatabase.getId(),
                    urlFromDatabase.getLongUrl(), new Timestamp(System.currentTimeMillis())));
        } else {
            List<UrlCache> urlCaches = urlCacheManager.cleanCache();

            updateUrl(urlCaches);
//            urlCacheManager.putInCache(urlShort, new UrlCache(urlFromDatabase.getId(),
//                    urlFromDatabase.getLongUrl(), new Timestamp(System.currentTimeMillis())));
        }
        return urlFromDatabase.getLongUrl();
    }

    @Override
    public void deleteUrl(String shortUrl) {
        urlRepository.deleteUrlByShortUrl(shortUrl);
    }

//    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateUrl(List<UrlCache> listForUpdate) {
        listForUpdate.forEach(urlCache -> urlRepository.updateUrlById(urlCache.getId(), urlCache.getLastUse()));
    }

    @Override
    public void cleanDatabase() {
        //также удаляет то что не исспользовалось 30 дней
        urlRepository.deleteOutdatedUrls();
    }
}

