package telrun.shortnik.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import telrun.shortnik.convertors.Convertors;
import telrun.shortnik.entity.Role;
import telrun.shortnik.entity.Url;
import telrun.shortnik.entity.User;
import telrun.shortnik.exception.ExceptionHandlers;
import telrun.shortnik.exception.NoPremiumRoleException;
import telrun.shortnik.generator.Generator;
import telrun.shortnik.repository.UrlRepository;
import telrun.shortnik.dto.UrlRequest;

import java.util.*;

/**
 * A service for working with the {@link Url} class.
 */
@Service
public class UrlServiceImpl implements UrlService {
    /**
     * A repository for working with the {@link Url} class.
     */
    private final UrlRepository urlRepository;
    /**
     * Converters necessary for transforming DTO, ENTITY, and vice versa.
     */
    private final Convertors convertors;
    /**
     * A generator for creating short URLs.
     */
    private final Generator generator;
    /**
     * The maximum allowed number of links for one user without a premium_role.
     */
    private final static int MAX_URLS = 30;
    /**
     * The maximum number of hours a link can be stored for a regular user.
     */
    private final static int URL_LIFE_TIME_HOURS = 720; //30days
    /**
     * A conditional paid premium role in the application that allows having links without time and quantity restrictions,
     * and in the future, it will provide access to additional statistics data.
     */
    private static final Role PREMIUM_ROLE = new Role(2L, "ROLE_PREMIUM", null);

    private static final Logger LOGGER = LogManager.getLogger(ExceptionHandlers.class);

    @Autowired
    public UrlServiceImpl(UrlRepository urlRepository, Convertors convertors, Generator generator) {
        this.urlRepository = urlRepository;
        this.convertors = convertors;
        this.generator = generator;
    }

    /**
     * Create a new link in the database.
     *
     * @param urlRequest An object containing information about the url to be created by the user.
     * @param user       An object obtained from the authentication context representing the user using the app.
     * @return The short generated link.
     */
    @Override
    public String createUrl(UrlRequest urlRequest, User user) {
        if (!user.getRoles().contains(PREMIUM_ROLE)) {
            int quantityUrlsOfCurrentUser = urlRepository.countAllByUser(user);
            if (quantityUrlsOfCurrentUser >= MAX_URLS) {
                LOGGER.warn("max number url for user " + user.getName());
                throw new NoPremiumRoleException(MAX_URLS + " is the maximum number of links for a user. If you want more, please upgrade your account to premium or either wait for the program to free up space");
            }
        }
        Url urlEntityForSave = convertors.requestToEntityUrl(urlRequest);
        urlEntityForSave.setShortUrl(generator.generate());
        urlEntityForSave = urlRepository.save(urlEntityForSave);
        LOGGER.info("a new url created " + urlEntityForSave);

        return urlEntityForSave.getShortUrl();
    }

    /**
     * This method searches the database for the corresponding original link using a short alias. Для возвращаемого
     Caching in memory is used for optimizing read operations for the return value.
     *
     * @param urlShort The short alias previously created by the application and stored in the database.
     * @return The original link for further redirection.
     */
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

    /**
     * A method for deleting a URL based on its corresponding short alias. The method also removes the URL from the application's cache.
     *
     * @param shortUrl The short alias previously created by the application and stored in the database.
     */
    @CacheEvict(cacheNames = "url")
    @Override
    public void deleteUrl(String shortUrl) {
        urlRepository.deleteUrlByShortUrl(shortUrl);
    }

    /**
     * This method clears the database of outdated URLs. URLs older than the class's static variable
     * {@link UrlServiceImpl#URL_LIFE_TIME_HOURS}.
     */
    @Override
    @CacheEvict(cacheNames = "url")
    public void cleanDatabase() {
        urlRepository.deleteOutdatedUrls(URL_LIFE_TIME_HOURS);
    }
}

