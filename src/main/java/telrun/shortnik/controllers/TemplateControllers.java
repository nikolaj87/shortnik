package telrun.shortnik.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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

    @Autowired
    public TemplateControllers(UserService userService, UrlService urlService) {
        this.userService = userService;
        this.urlService = urlService;
    }

    @GetMapping("/")
    public String mainPage() {
        return "main";
    }

    @PostMapping("/main")
    public String createUrl(@ModelAttribute @Valid UrlRequest urlRequest, @AuthenticationPrincipal User user) {
        urlRequest.setUser(user);
        String shortUrl = urlService.createUrl(urlRequest);
        urlRequest.setLongUrl("http://localhost:8080/" + shortUrl);
        urlRequest.setDescription("");
        return "main";
    }

    @GetMapping("/login")
    public String homePage() {
        return "login";
    }

    @PostMapping("/login")
    public String loginSubmit() {
        return "main";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String createUser(@ModelAttribute UserRequest userRequest) {
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
