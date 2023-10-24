package telrun.shortnik.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import telrun.shortnik.convertors.Convertors;
import telrun.shortnik.entity.Role;
import telrun.shortnik.entity.Url;
import telrun.shortnik.entity.User;
import telrun.shortnik.exception.NoPremiumRoleException;
import telrun.shortnik.generator.Generator;
import telrun.shortnik.repository.UrlRepository;
import telrun.shortnik.dto.UrlRequest;

import java.util.*;

@Service
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;
    private final Convertors convertors;
    private final Generator generator;
    private final static int MAX_URLS = 30;
    private final static int URL_LIFE_TIME_HOURS = 720; //30days
    private static final Role PREMIUM_ROLE = new Role(2L, "ROLE_PREMIUM", null);

    @Autowired
    public UrlServiceImpl(UrlRepository urlRepository, Convertors convertors, Generator generator) {
        this.urlRepository = urlRepository;
        this.convertors = convertors;
        this.generator = generator;
    }

    @Override
    public String createUrl(UrlRequest urlRequest, User user) {
        if (!user.getRoles().contains(PREMIUM_ROLE)) {
            int quantityUrlsOfCurrentUser = urlRepository.countAllByUser(user);
            if (quantityUrlsOfCurrentUser >= MAX_URLS)
                throw new NoPremiumRoleException(MAX_URLS + " is the maximum number of links for a user. If you want more, please upgrade your account to premium or either wait for the program to free up space");
        }
        Url urlEntityForSave = convertors.requestToEntityUrl(urlRequest);
        urlEntityForSave.setShortUrl(generator.generate());
        urlRepository.save(urlEntityForSave);

        return urlEntityForSave.getShortUrl();
    }

    @Cacheable(cacheNames = "url")
    @Override
    public String getLongUrlByShortName(String urlShort) {
        Optional<Url> urlFromDatabaseOptional = urlRepository.findByShortUrl(urlShort);
        if (urlFromDatabaseOptional.isEmpty()) {
            return "/";
        }
        Url urlFromDatabase = urlFromDatabaseOptional.get();
        return urlFromDatabase.getLongUrl();
    }

    @CacheEvict(cacheNames = "url")
    @Override
    public void deleteUrl(String shortUrl) {
        urlRepository.deleteUrlByShortUrl(shortUrl);
    }

    @Override
    @CacheEvict(cacheNames = "url")
    public void cleanDatabase() {
        urlRepository.deleteOutdatedUrls(URL_LIFE_TIME_HOURS);
    }
}

