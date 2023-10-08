package telrun.shortnik.controllers.api;

import com.google.gson.reflect.TypeToken;
import net.bytebuddy.description.method.MethodDescription;
import org.jsoup.Connection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import telrun.shortnik.dto.UserRequest;
import telrun.shortnik.dto.UserResponse;
import telrun.shortnik.entity.Role;
import telrun.shortnik.repository.UserRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class UserControllersTest {

    @Autowired
    private GsoupHttpConnector connector;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JsonCreator jsonCreator;

    @BeforeEach
    public void methodBefore () {
        userRepository.deleteAll();
    }
    @AfterEach
    public void clearDb () {
        userRepository.deleteAll();
    }

    @Test
    void whenCreateUser_thanUserIsCreated() throws IOException {
        String userJson = jsonCreator.createJson(new UserRequest("testName", "testPassword", "testEmail"));

        Connection.Response resultOfPostRequest = connector.postRequestJson(userJson, "user");
        Connection.Response resultOfGetRequest = connector.getRequestJson("user");
        List<UserResponse> allUsersFromDatabase = jsonCreator.convertJsonToObject(resultOfGetRequest.body(),
                new TypeToken<List<UserResponse>>() {}.getType());

        assertEquals(1, allUsersFromDatabase.size());
        assertEquals(HttpStatus.CREATED.value(), resultOfPostRequest.statusCode());
        assertEquals(HttpStatus.OK.value(), resultOfGetRequest.statusCode());
        assertTrue(resultOfGetRequest.body().contains("testName"));
        assertTrue(resultOfGetRequest.body().contains("testEmail"));
        assertTrue(allUsersFromDatabase.get(0).getRoles().contains(new Role(3L, "USER", null)));
    }

    @Test
    void whenDeleteUser_thanDbIsEmpty() throws IOException {
        String userJson = jsonCreator.createJson(new UserRequest("testName2", "testPassword2", "testEmail2"));

        connector.postRequestJson(userJson, "user");
        Connection.Response resultDelete = connector.deleteRequestJson("user/testName2");
        Connection.Response resultGet = connector.getRequestJson("user");
        List<UserResponse> allUsers = jsonCreator.convertJsonToObject(resultGet.body()
        , new TypeToken<List<UserResponse>>() {}.getType());

        assertEquals(HttpStatus.NO_CONTENT.value(), resultDelete.statusCode());
        assertEquals(0, allUsers.size());
    }
}