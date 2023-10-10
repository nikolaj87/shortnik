package telrun.shortnik.controllers.api;

import org.jsoup.Connection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

    @Autowired
    private GsoupHttpConnector connector;
    @Autowired
    private JsonCreator jsonCreator;
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
        testUser = userRepository.save(new User(0L, "testUser", "testPassword", "testEmail",
                new Timestamp(System.currentTimeMillis()), Set.of(new Role(3L, "USER", null))));
        testUser.setRegisteredAt(null);
    }

    @AfterEach
    public void clearDb() {
        urlRepository.deleteAll();
        userRepository.deleteAll();
    }

//    @Test
//    void whenCreateUrlInDatabase_canGetItByOriginalName() throws IOException {
//        String urlRequestJson = jsonCreator.createJson(new UrlRequest(testOriginalUrl, "some description", testUser));
//
//        Connection.Response resultOfPostRequest = connector.postRequestJson(urlRequestJson, "url");
//
//        assertEquals(201, resultOfPostRequest.statusCode());
//    }
    @Test
    void mustCreateShortUrl_andRedirectToOriginalAfterRequest() throws IOException {
        String urlRequestJson = jsonCreator.createJson(new UrlRequest(testOriginalUrl, "some description", testUser));
        Connection.Response resultOfPostRequest = connector.postRequestJson(urlRequestJson, "url");

        Connection.Response resultOfGetRequest = connector.getRequestJson(resultOfPostRequest.body());

        assertEquals(testOriginalUrl, resultOfGetRequest.url().toString());
    }

    @Test
    void mustCreateShortUrl_andDeleteIt() throws IOException {
        String urlRequestJson = jsonCreator.createJson(new UrlRequest(testOriginalUrl, "some description", testUser));
        Connection.Response resultOfPostRequest = connector.postRequestJson(urlRequestJson, "url");
        String savedShortUrl = resultOfPostRequest.body();

        Connection.Response resultOfDeleteRequest = connector.deleteRequestJson("delete/" + savedShortUrl);
        List<Url> allUrl = urlRepository.findAll();

        assertEquals(HttpStatus.NO_CONTENT.value(), resultOfDeleteRequest.statusCode());
        assertTrue(allUrl.isEmpty());
    }
}