package telrun.shortnik.controllers.api;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import telrun.shortnik.dto.UserRequest;
import telrun.shortnik.dto.UserResponse;
import telrun.shortnik.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserControllers {
    private final UserService userService;

    @Autowired
    public UserControllers(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserResponse createUser(@Valid @RequestBody UserRequest userRequest) {
        return userService.createUser(userRequest);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{name}")
    public void deleteUser(@PathVariable String name) {
        userService.deleteUser(name);
    }

    @GetMapping("/get-page")
    public List<UserResponse> getUsersOnPage(@RequestParam(name = "page", defaultValue = "0") int page) {
        PageRequest pageRequest = PageRequest.of(page, 30);
        return userService.getAllUsersOnPage(pageRequest);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{userId}")
    public void addRolePremium(@PathVariable Long userId) {
        userService.addPremiumRole(userId);
    }
}
