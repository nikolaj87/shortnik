package telrun.shortnik.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import telrun.shortnik.dto.UrlRequest;
import telrun.shortnik.dto.UserRequest;
import telrun.shortnik.entity.User;
import telrun.shortnik.service.UrlService;
import telrun.shortnik.service.UserService;


@Controller
@RequestMapping
public class TemplateControllers {

    private final UserService userService;
    private final UrlService urlService;
    private static final String SERVER_URL = "http://localhost:8080/";

    @Autowired
    public TemplateControllers(UserService userService, UrlService urlService) {
        this.userService = userService;
        this.urlService = urlService;
    }

    /**
     * Render the main page using HTTP GET method.
     * This controller serves the path "/".
     *
     * @return The "main" view.
     */
    @GetMapping("/")
    public String mainPage() {
        return "main";
    }

    /**
     * Render the main page using HTTP GET method.
     * This controller serves the path and "/main".
     *
     * @return The "main" view.
     */

    @GetMapping("/main")
    public String mainPage2() {
        return "main";
    }

    /**
     * Create a new URL using HTTP POST method.
     * This controller serves the path "/main".
     *
     * @param urlRequest The request containing URL details.
     * @param errors     Errors that might occur during form submission.
     * @param user       The authenticated user.
     * @return The "main" view or the created URL.
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/main")
    public String createUrl(@Valid @ModelAttribute UrlRequest urlRequest, Errors errors, @AuthenticationPrincipal User user) {
        if (errors.hasErrors()) return "main";
        urlRequest.setUser(user);
        String shortUrl = urlService.createUrl(urlRequest, user);
        urlRequest.setLongUrl(SERVER_URL + shortUrl);
        urlRequest.setDescription("");
        return "main";
    }

    /**
     * Redirect to the original long URL using HTTP GET method.
     *
     * @param urlShort The short URL.
     * @return A RedirectView to the original long URL.
     */
    @GetMapping("/{urlShort}")
    public RedirectView redirectToLongUrl(@PathVariable String urlShort) {
        String originalUrl = urlService.getLongUrlByShortName(urlShort);
        return new RedirectView(originalUrl);
    }

    /**
     * Render the login page using HTTP GET method.
     * This controller serves the path "/login".
     *
     * @return The "login" view.
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @GetMapping("/login")
    public String homePage() {
        return "login";
    }

    /**
     * Process the login form using HTTP POST method.
     * This controller serves the path "/login".
     *
     * @return The "main" view upon successful login.
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public String loginSubmit() {
        return "main";
    }

    /**
     * Render the registration page using HTTP GET method.
     * This controller serves the path "/register".
     *
     * @return The "register" view.
     */
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    /**
     * Create a new user using HTTP POST method.
     * This controller serves the path "/register".
     *
     * @param userRequest The request containing user details.
     * @param errors      Errors that might occur during form submission.
     * @return The "register" view or the "login" view upon successful registration.
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public String createUser(@Valid @ModelAttribute UserRequest userRequest, Errors errors) {
        if (errors.hasErrors()) return "register";
        userService.createUser(userRequest);
        return "login";
    }

    @ModelAttribute("userRequest")
    public UserRequest addUserRequestToModel() {
        return new UserRequest();
    }

    @ModelAttribute("urlRequest")
    public UrlRequest addUrlRequestToModel() {
        return new UrlRequest();
    }

}
