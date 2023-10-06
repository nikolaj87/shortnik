package telrun.shortnik.controllers.api;

import com.google.gson.Gson;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import telrun.shortnik.dto.UrlRequest;
import telrun.shortnik.entity.Role;
import telrun.shortnik.entity.Url;
import telrun.shortnik.entity.User;
import telrun.shortnik.repository.UrlRepository;
import telrun.shortnik.repository.UserRepository;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class UrlControllersTest {

    @LocalServerPort
    private int port;
    private String url;
    private final String testOriginalUrl = "https://www.google.com/";
    @Autowired
    private UrlRepository urlRepository;
    @Autowired
    private UserRepository userRepository;
    private User testUser;

    @BeforeEach
    public void methodBefore() {
        urlRepository.deleteAll();
        userRepository.deleteAll();
        url = "http://localhost:" + port;
        testUser = userRepository.save(new User(0L, "testUser", "testPassword", "testEmail",
                new Timestamp(System.currentTimeMillis()), Set.of(new Role(3L, "USER", null))));
        testUser.setRegisteredAt(null);
    }

    @AfterEach
    public void clearDb() {
        urlRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    void whenCreateUrlInDatabase_canGetItByOriginalName() throws IOException {

        String urlRequestJson = createJson(new UrlRequest(testOriginalUrl, "some description", testUser));

        Connection.Response resultOfPostRequest = postNewUrl(urlRequestJson);

        assertEquals(201, resultOfPostRequest.statusCode());
    }
    @Test
    void mustCreateShortUrl_andRedirectToOriginalAfterRequest() throws IOException {
        String urlRequestJson = createJson(new UrlRequest(testOriginalUrl, "some description", testUser));
        Connection.Response resultOfPostRequest = postNewUrl(urlRequestJson);

        Connection.Response resultOfGetRequest = redirectToOriginalUrl(resultOfPostRequest.body());

        assertEquals(testOriginalUrl, resultOfGetRequest.url().toString());
    }

    @Test
    void mustCreateShortUrl_andDeleteIt() throws IOException {
        String urlRequestJson = createJson(new UrlRequest(testOriginalUrl, "some description", testUser));
        Connection.Response resultOfPostRequest = postNewUrl(urlRequestJson);
        String savedShortUrl = resultOfPostRequest.body();

        Connection.Response resultOfDeleteRequest = deleteUrlByShortUrl(savedShortUrl);

        assertEquals(HttpStatus.NO_CONTENT.value(), resultOfDeleteRequest.statusCode());
    }

    private Connection.Response deleteUrlByShortUrl(String shortUrl) throws IOException {
        return Jsoup.connect(url + "/delete/" + shortUrl)
                .ignoreContentType(true)
                .method(Connection.Method.DELETE)
                .execute();
    }

    private Connection.Response redirectToOriginalUrl(String body) throws IOException {
        return Jsoup.connect(url + "/" + body)
                .ignoreContentType(true)
                .method(Connection.Method.GET)
                .execute();
    }

    private Connection.Response getShotUrlByLongName(String originalLongUrl) throws IOException {
        return Jsoup.connect(url + "/long/" + originalLongUrl)
                .ignoreContentType(true)
                .method(Connection.Method.GET)
                .execute();
    }

    private Connection.Response postNewUrl(String urlRequestJson) throws IOException {
        return Jsoup.connect(url + "/url")
                .ignoreContentType(true)
                .header("Content-Type", "application/json")
                .method(Connection.Method.POST)
                .requestBody(urlRequestJson)
                .execute();
    }

    private String createJson(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }
}