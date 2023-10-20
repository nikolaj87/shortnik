package telrun.shortnik.controllers.api;

import com.google.gson.reflect.TypeToken;
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
import telrun.shortnik.service.UserService;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class UserControllersTest {

    @Autowired
    private GsoupHttpConnector connector;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private JsonCreator jsonCreator;
    private final UserRequest testUser = new UserRequest("Nik", "1Q.niki_password", "test@some_mail");

    @BeforeEach
    public void methodBefore() {
        userService.deleteUser(testUser.getName());
    }
    @AfterEach
     public void clearDb () {
        userService.deleteUser(testUser.getName());
    }

    @Test
    void whenCreateUser_thanUserIsCreated_noAuthentication() throws IOException {
        var jsonUserForSave = jsonCreator.createJson(testUser);

        Connection.Response resultOfCreateUserRequest = connector.postRequestJson(jsonUserForSave, "user");

        assertTrue(userRepository.findUserByName("Nik").isPresent());
        assertEquals(HttpStatus.CREATED.value(), resultOfCreateUserRequest.statusCode());
    }

    @Test
    void mustNotDeleteUser_noAuthUserAuth() throws IOException {
        Connection.Response addingTestUserToDatabase = connector.postRequestJson(jsonCreator.createJson(testUser), "user");

        Connection.Response tryDeleteUserNoAuth = connector.deleteRequestJson("user/" + testUser.getName());
        Connection.Response userIsLoggingApp = connector.postRequestHtml("user", "user", "login");
        Connection.Response tryDeleteUser = connector.deleteRequestJson("user/" + testUser.getName(), userIsLoggingApp.cookies());

        assertTrue(userRepository.findUserByName("Nik").isPresent());
        assertEquals(HttpStatus.FORBIDDEN.value(), tryDeleteUser.statusCode());
        assertEquals(HttpStatus.UNAUTHORIZED.value(), tryDeleteUserNoAuth.statusCode());

    }

    @Test
    void mustDeleteUser_adminAuthentication() throws IOException {
        Connection.Response addingTestUserToDatabase = connector.postRequestJson(jsonCreator.createJson(testUser), "user");

        Connection.Response adminIsLoggingApp = connector.postRequestHtml("admin", "admin", "login");
        Connection.Response mustDeleteUser = connector.deleteRequestJson("user/" + testUser.getName(), adminIsLoggingApp.cookies());

        assertTrue(userRepository.findUserByName("Nik").isEmpty());
        assertEquals(HttpStatus.NO_CONTENT.value(), mustDeleteUser.statusCode());
    }

    @Test
    void mustNotGetUsers_noAuthentication() throws IOException {

        Connection.Response tryGetUsersNoAuth = connector.getRequestJson("user");
        Connection.Response userIsLoggingApp = connector.postRequestHtml("user", "user", "login");
        Connection.Response tryGetUsersUserAuth = connector.getRequestJson("user", userIsLoggingApp.cookies());

        assertEquals(HttpStatus.UNAUTHORIZED.value(), tryGetUsersNoAuth.statusCode());
        assertEquals(HttpStatus.FORBIDDEN.value(), tryGetUsersUserAuth.statusCode());
        assertTrue(tryGetUsersNoAuth.body().contains("<title>shortnik_login</title>"));
    }

    @Test
    void mustAllowGetUsers_Admin() throws IOException {

        Connection.Response adminIsLoggingApp = connector.postRequestHtml("admin", "admin", "login");
        Connection.Response mustGetUsersAdminAuth = connector.getRequestJson("user", adminIsLoggingApp.cookies());
        List<UserResponse> allUsersFromDatabase = jsonCreator.convertJsonToObject(mustGetUsersAdminAuth.body(),
                new TypeToken<List<UserResponse>>() {}.getType());

        assertEquals(HttpStatus.OK.value(), mustGetUsersAdminAuth.statusCode());
        assertFalse(allUsersFromDatabase.isEmpty());
    }

    @Test
    void canNotAddPremiumRole_noAuth() throws IOException {
        Connection.Response addingTestUserToDatabase = connector.postRequestJson(jsonCreator.createJson(testUser), "user");
        long savedUserId = jsonCreator.convertJsonToObject(addingTestUserToDatabase.body(), new UserResponse()).getId();

        Connection.Response tryAddPremiumRole = connector.postRequestJson("", "user/" + savedUserId);
        Set<Role> rolesResult = userRepository.findById(savedUserId).orElseThrow().getRoles();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), tryAddPremiumRole.statusCode());
        assertFalse(rolesResult.contains(new Role(2L, "ROLE_PREMIUM", null)));
    }

    @Test
    void mustAddPremiumRole_adminAuth() throws IOException {
        Connection.Response addingTestUserToDatabase = connector.postRequestJson(jsonCreator.createJson(testUser), "user");
        long savedUserId = jsonCreator.convertJsonToObject(addingTestUserToDatabase.body(), new UserResponse()).getId();

        Connection.Response adminIsLoggingApp = connector.postRequestHtml("admin", "admin", "login");
        Connection.Response tryAddPremiumRole = connector.postRequestJson("", "user/" + savedUserId, adminIsLoggingApp.cookies());
        Set<Role> rolesResult = userRepository.findById(savedUserId).orElseThrow().getRoles();

        assertTrue(rolesResult.contains(new Role(2L, "ROLE_PREMIUM", null)));
    }
}