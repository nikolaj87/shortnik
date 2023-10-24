package telrun.shortnik.controllers;

import org.jsoup.Connection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import telrun.shortnik.controllers.api.GsoupHttpConnector;
import telrun.shortnik.dto.UrlRequest;
import telrun.shortnik.dto.UrlResponse;
import telrun.shortnik.dto.UserRequest;
import telrun.shortnik.entity.Url;
import telrun.shortnik.entity.User;
import telrun.shortnik.repository.UrlRepository;
import telrun.shortnik.repository.UserRepository;
import telrun.shortnik.service.UrlService;
import telrun.shortnik.service.UserService;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class TemplateControllersTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UrlRepository urlRepository;
    @Autowired
    private UrlService urlService;
    @Autowired
    private UserService userService;
    @Autowired
    private GsoupHttpConnector connector;

    @Test
    void mustNotReturnMainPage_noAuth() throws IOException {

        Connection.Response tryRequestMainTemplate = connector.getRequestHtml("");

        assertEquals(401, tryRequestMainTemplate.statusCode());
        assertTrue(tryRequestMainTemplate.body().contains("<title>shortnik_login</title>"));
    }
    @Test
    void mustReturnMainPage_authUser() throws IOException {
        Connection.Response userIsLoggingApp = connector.postRequestHtml("user", "user", "login");

        Connection.Response requestMainTemplate = connector.getRequestHtml("", userIsLoggingApp.cookies());

        assertEquals(200, requestMainTemplate.statusCode());
        assertTrue(requestMainTemplate.body().contains("<title>shortnik_main</title>"));
    }

    @Test
    void mustCreateShortUrlForAuthenticatedUser_andChangeUrlRequestFromModel() throws IOException {
        String testOriginalUrl = "https://www.booking.com/index.ru.html?label=gen173nr-1BCAEoggI46AdIM1gEaLYBiAEBmAEhuAEXyAEM2AEB6AEBiAIBqAIDuAKC1bSpBsACAdICJDI4OTdiZDcxLTQzYjktNDg1Ni1iMWE4LTg3ZWNkYzBhODNjZNgCBeACAQ&sid=ce60ff64916cc08b4b5fb2b3f80fc344&keep_landing=1&sb_price_type=total&";
        UrlRequest urlRequest = new UrlRequest(testOriginalUrl, "some text", null);
        Connection.Response userIsLogging = connector.postRequestHtml("user", "user", "login");

        Connection.Response mustCreateUrlInDatabase = connector.postRequestHtml(urlRequest, userIsLogging.cookies(), "main");
        Url createResultUrlFromDatabase = urlRepository.findAll().get(0);
        urlService.deleteUrl(createResultUrlFromDatabase.getShortUrl());

        assertEquals(201, mustCreateUrlInDatabase.statusCode());
        assertEquals(testOriginalUrl, createResultUrlFromDatabase.getLongUrl());
        assertTrue(mustCreateUrlInDatabase.body().contains("required value=\"http://localhost:"));
    }

    @Test
    void mustReturnLoginPage_noAuth() throws IOException {

        Connection.Response getRequestLoginTemplate = connector.getRequestHtml("login");

        assertTrue(getRequestLoginTemplate.body().contains("<title>shortnik_login</title>"));
    }

    @Test
    void mustReturnRegisterPage_noAuth() throws IOException {

        Connection.Response requestRegisterPage = connector.getRequestHtml("register");

        assertEquals(200, requestRegisterPage.statusCode());
        assertTrue(requestRegisterPage.body().contains("<title>shortnik_registration</title>"));
    }

    @Test
    void mustCreateNewUserAndReturnLoginPage() throws IOException {
        UserRequest userRequest = new UserRequest("someName", "somePassword123/", "some@email");

        Connection.Response postRequestRegisterPage = connector.postRequestHtml(userRequest, "register");
        Optional<User> resultUserFromDatabase = userRepository.findUserByName("someName");
        userService.deleteUser(userRequest.getName());

        assertTrue(resultUserFromDatabase.isPresent());
        assertTrue(postRequestRegisterPage.body().contains("<title>shortnik_login</title>"));
    }

    @Test
    void mustRedirectToOriginalUrl_noAuth() throws IOException {
        String testOriginalUrl = "https://www.booking.com/index.ru.html?label=gen173nr-1BCAEoggI46AdIM1gEaLYBiAEBmAEhuAEXyAEM2AEB6AEBiAIBqAIDuAKC1bSpBsACAdICJDI4OTdiZDcxLTQzYjktNDg1Ni1iMWE4LTg3ZWNkYzBhODNjZNgCBeACAQ&sid=ce60ff64916cc08b4b5fb2b3f80fc344&keep_landing=1&sb_price_type=total&";
        UrlRequest urlRequest = new UrlRequest(testOriginalUrl, "some text", null);
        Connection.Response userIsLogging = connector.postRequestHtml("user", "user", "login");

        Connection.Response mustCreateUrlInDatabase = connector.postRequestHtml(urlRequest, userIsLogging.cookies(), "main");
        Url createResultUrlFromDatabase = urlRepository.findAll().get(0);
        Connection.Response mustSuccessRedirectNoAuth = connector.getRequestJson(createResultUrlFromDatabase.getShortUrl());
        urlService.deleteUrl(createResultUrlFromDatabase.getShortUrl());

        assertEquals(HttpStatus.OK.value(), mustSuccessRedirectNoAuth.statusCode());
        assertEquals(testOriginalUrl, mustSuccessRedirectNoAuth.url().toString());
    }
}