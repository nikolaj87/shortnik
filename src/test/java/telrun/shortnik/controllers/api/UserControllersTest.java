package telrun.shortnik.controllers.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import telrun.shortnik.dto.UserRequest;
import telrun.shortnik.dto.UserResponse;
import telrun.shortnik.entity.Role;
import telrun.shortnik.entity.User;
import telrun.shortnik.repository.UserRepository;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class UserControllersTest {

    @LocalServerPort
    private int port;
    private String url;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRepository urlRepository;

    @BeforeEach
    public void methodBefore () {
        userRepository.deleteAll();
        url = "http://localhost:" + port + "/user";
    }
    @AfterEach
    public void clearDb () {
        userRepository.deleteAll();
    }

    @Test
    void whenCreateUser_thanUserIsCreated() throws IOException {
        String userJson = createJson(new UserRequest("testName", "testPassword", "testEmail"));

        Connection.Response resultOfPostRequest = postNewUserJson(userJson);
        Connection.Response resultOfGetRequest = getAllUsersJson();
        List<UserResponse> allUsersFromDatabase = resultOfGetRequestConverToList(resultOfGetRequest.body());

        assertEquals(1, allUsersFromDatabase.size());
        assertEquals(HttpStatus.CREATED.value(), resultOfPostRequest.statusCode());
        assertEquals(HttpStatus.OK.value(), resultOfGetRequest.statusCode());
        assertTrue(resultOfGetRequest.body().contains("testName"));
        assertTrue(resultOfGetRequest.body().contains("testEmail"));
        assertTrue(allUsersFromDatabase.get(0).getRoles().contains(new Role(3L, "USER", null)));
    }

    @Test
    void whenDeleteUser_thanDbIsEmpty() throws IOException {
        String userJson = createJson(new UserRequest("testName2", "testPassword2", "testEmail2"));

        postNewUserJson(userJson);
        Connection.Response resultDelete = deleteUserJson("testName2");
        Connection.Response resultGet = getAllUsersJson();
        List<UserResponse> allUsers = resultOfGetRequestConverToList(resultGet.body());

        assertEquals(HttpStatus.NO_CONTENT.value(), resultDelete.statusCode());
        assertEquals(0, allUsers.size());
    }

    private Connection.Response deleteUserJson(String nameForDelete) throws IOException {
        return Jsoup.connect(url + "/" + nameForDelete)
                .ignoreContentType(true)
                .method(Connection.Method.DELETE)
                .execute();
    }

    private Connection.Response getAllUsersJson() throws IOException {
        return Jsoup.connect(url)
                .ignoreContentType(true)
                .method(Connection.Method.GET)
                .execute();
    }

    private Connection.Response postNewUserJson(String userJson) throws IOException {
        return Jsoup.connect(url)
                .ignoreContentType(true)
                .header("Content-Type", "application/json")
                .method(Connection.Method.POST)
                .requestBody(userJson)
                .execute();
    }

    private List<UserResponse> resultOfGetRequestConverToList(String body) {
        Gson gson = new Gson();
        Type userListType = new TypeToken<List<UserResponse>>() {}.getType();
        return gson.fromJson(body, userListType);
    }

    private String createJson(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }
}