package telrun.shortnik.controllers.api;

import org.jsoup.Connection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import telrun.shortnik.dto.UrlRequest;
import telrun.shortnik.dto.UrlResponse;
import telrun.shortnik.entity.Role;
import telrun.shortnik.entity.Url;
import telrun.shortnik.entity.User;
import telrun.shortnik.repository.UrlRepository;
import telrun.shortnik.repository.UserRepository;
import telrun.shortnik.service.UrlService;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class UrlControllersTest {

    @Autowired
    private GsoupHttpConnector connector;
    @Autowired
    private JsonCreator jsonCreator;
    private final String testOriginalUrl = "https://www.booking.com/index.ru.html?label=gen173nr-1BCAEoggI46AdIM1gEaLYBiAEBmAEhuAEXyAEM2AEB6AEBiAIBqAIDuAKC1bSpBsACAdICJDI4OTdiZDcxLTQzYjktNDg1Ni1iMWE4LTg3ZWNkYzBhODNjZNgCBeACAQ&sid=ce60ff64916cc08b4b5fb2b3f80fc344&keep_landing=1&sb_price_type=total&";
    @Autowired
    private UrlRepository urlRepository;
    @Autowired
    private UrlService urlService;
    private String shortUrlForTests;

    @BeforeEach
    public void methodBefore() {
    }

    @AfterEach
    public void clearDb() {
        urlService.deleteUrl(shortUrlForTests);
    }

    @Test
    void mustNotCreateShortUrl_noAuth() throws IOException {
        var urlRequest = new UrlRequest(testOriginalUrl, "some description", null);
        String urlRequestJson = jsonCreator.createJson(urlRequest);

        Connection.Response tryToAddUrl = connector.postRequestJson(urlRequestJson, "url");

        assertEquals(HttpStatus.UNAUTHORIZED.value(), tryToAddUrl.statusCode());
    }

    @Test
    void mustAllowCreateShortUrl_withAuth() throws IOException {
        var urlRequest = new UrlRequest(testOriginalUrl, "some description", null);
        String urlRequestJson = jsonCreator.createJson(urlRequest);

        Connection.Response userIsLoggingApp = connector.postRequestHtml("user", "user", "login");
        Connection.Response mustAddUrl = connector.postRequestJson(urlRequestJson, "url", userIsLoggingApp.cookies());
        Url savedUrl = urlRepository.findByShortUrl(mustAddUrl.body()).orElseThrow();
        shortUrlForTests = savedUrl.getShortUrl();

        assertEquals(HttpStatus.CREATED.value(), mustAddUrl.statusCode());
        assertEquals(testOriginalUrl, savedUrl.getLongUrl());
    }

    @Test
    void mustRedirectToOriginalUrl_noAuth() throws IOException {
        var urlRequest = new UrlRequest(testOriginalUrl, "some description", null);
        String urlRequestJson = jsonCreator.createJson(urlRequest);
        Connection.Response userIsLoggingApp = connector.postRequestHtml("user", "user", "login");
        Connection.Response mustAddUrl = connector.postRequestJson(urlRequestJson, "url", userIsLoggingApp.cookies());
        shortUrlForTests = mustAddUrl.body();

        Connection.Response mustSuccessRedirectNoAuth = connector.getRequestJson("url/" + shortUrlForTests);
        var urlResponse = jsonCreator.convertJsonToObject(mustSuccessRedirectNoAuth.body(), new UrlResponse());

        assertEquals(HttpStatus.OK.value(), mustSuccessRedirectNoAuth.statusCode());
        assertEquals(testOriginalUrl, urlResponse.getLongUrl());
    }

    @Test
    void mustNotAllowDeleteUrl_noAuth() throws IOException {
        var urlRequest = new UrlRequest(testOriginalUrl, "some description", null);
        String urlRequestJson = jsonCreator.createJson(urlRequest);
        Connection.Response userIsLoggingApp = connector.postRequestHtml("user", "user", "login");
        Connection.Response mustAddUrl = connector.postRequestJson(urlRequestJson, "url", userIsLoggingApp.cookies());
        shortUrlForTests = mustAddUrl.body();

        Connection.Response tryDeleteUrlNoAuth = connector.deleteRequestJson("url/delete/" + shortUrlForTests);
        Optional<Url> isDeletedUrl = urlRepository.findByShortUrl(shortUrlForTests);

        assertEquals(HttpStatus.UNAUTHORIZED.value(), tryDeleteUrlNoAuth.statusCode());
        assertTrue(isDeletedUrl.isPresent());
    }

    @Test
    void mustAllowDeleteUrl_adminAuth() throws IOException {
        var urlRequest = new UrlRequest(testOriginalUrl, "some description", null);
        String urlRequestJson = jsonCreator.createJson(urlRequest);

        Connection.Response userIsLoggingApp = connector.postRequestHtml("user", "user", "login");
        Connection.Response mustAddUrl = connector.postRequestJson(urlRequestJson, "url", userIsLoggingApp.cookies());
        shortUrlForTests = mustAddUrl.body();

        Connection.Response adminIsLoggingApp = connector.postRequestHtml("admin", "admin", "login");
        Connection.Response tryDeleteUrlNoAuth = connector.deleteRequestJson("url/delete/" + shortUrlForTests, adminIsLoggingApp.cookies());
        Optional<Url> isDeletedUrl = urlRepository.findByShortUrl(shortUrlForTests);

        assertEquals(HttpStatus.NO_CONTENT.value(), tryDeleteUrlNoAuth.statusCode());
        assertTrue(isDeletedUrl.isEmpty());
    }
}