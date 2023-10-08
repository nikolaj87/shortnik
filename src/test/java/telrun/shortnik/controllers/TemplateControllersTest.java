package telrun.shortnik.controllers;

import org.jsoup.Connection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import telrun.shortnik.controllers.api.GsoupHttpConnector;
import telrun.shortnik.entity.Role;
import telrun.shortnik.entity.User;
import telrun.shortnik.repository.UserRepository;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
//Помогите разобраться я застрял. Я пишу тест. Я запрашиваю MAIN шаблон. Для этого мой USER должен быть аутентифицированным
//в тесте @BEFOREEACH я создраю USER, добавляю его в БД, и добавляю его в контекст аутентификации.
// Позже в тесте void mustReturnMainPageForAuthenticatedUser() я запрашиваю шаблон MAIN от имени USER, который является
// atuthenticated = TRUE.  Но вместо шаблона MAIN идет переадресация на REGISTER будто USER неаутентифицирован.
// не могу понять почему так происходит
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class TemplateControllersTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GsoupHttpConnector connector;

    @BeforeEach
    public void initUserAuthentication() {
        userRepository.deleteAll();
        UserDetails testUser = userRepository.save(new User(0L, "testUser", "testPassword", "testEmail",
                new Timestamp(System.currentTimeMillis()), Set.of(new Role(3L, "USER", null))));
        Authentication authentication = new UsernamePasswordAuthenticationToken(testUser.getUsername(),
                testUser.getPassword(), testUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @AfterEach
    public void cleanDatabase() {
        userRepository.deleteAll();
    }

    @Test //почему идет переадресация на ЛОГИН ведь юзер аутентифицирован
    void mustReturnMainPageForAuthenticatedUser() throws IOException {
//        аутентификация прошла, user atuthenticated = TRUE
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println(authentication);

        Connection.Response getRequestMainTemplate = connector.getRequestXml(""); //запрос шаблона MAIN
//        System.out.println(getRequestMainTemplate.body());

        assertEquals(200, getRequestMainTemplate.statusCode());
        assertTrue(getRequestMainTemplate.body().contains("<title>shortnik_main</title>"));  //должен вернуть MaIN шаблон
        //но в ответе ШАБЛОН REGISTER
    }

    @Test //почему идет переадресация на ЛОГИН ведь юзер аутентифицрован
    void mustCutOriginalUrlAtTemplate() throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);

        Connection.Response postRequestMainTemplate = connector.postRequestXml("main");

        System.out.println(postRequestMainTemplate.body());
        assertEquals(200, postRequestMainTemplate.statusCode());

    }

    @Test
    void mustReturnLoginPage() throws IOException {
        Connection.Response getRequestLoginTemplate = connector.getRequestXml("login");

        System.out.println(getRequestLoginTemplate.body());


        assertEquals(200, getRequestLoginTemplate.statusCode());
        assertTrue(getRequestLoginTemplate.body().contains("<title>shortnik_login</title>"));
    }

    @Test
    void loginSubmit() {
    }

    @Test
    void mustReturnRegisterPage() throws IOException {

        Connection.Response requestRegisterPage = connector.getRequestXml("register");

        assertEquals(200, requestRegisterPage.statusCode());
        assertTrue(requestRegisterPage.body().contains("<title>shortnik_registration</title>"));
    }

    @Test
    void createUser() {
    }

    @Test
    void addUserRequestToModel() {
    }

    @Test
    void addUrlRequestToModel() {
    }
}