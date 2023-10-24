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

    @GetMapping("/")
    public String mainPage() {
        return "main";
    }

    @GetMapping("/main")
    public String mainPage2() {
        return "main";
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/main")
    public String createUrl(@Valid @ModelAttribute UrlRequest urlRequest,  Errors errors, @AuthenticationPrincipal User user) {
        if (errors.hasErrors()) return "main";
        urlRequest.setUser(user);
        String shortUrl = urlService.createUrl(urlRequest, user);
        urlRequest.setLongUrl(SERVER_URL + shortUrl);
        urlRequest.setDescription("");
        return "main";
    }

    @GetMapping("/{urlShort}")
    public RedirectView redirectToLongUrl (@PathVariable String urlShort) {
        String originalUrl = urlService.getLongUrlByShortName(urlShort);
        return new RedirectView(originalUrl);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @GetMapping("/login")
    public String homePage() {
        return "login";
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public String loginSubmit() {
        return "main";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

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
