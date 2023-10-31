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

    /**
     * Create a new user using HTTP POST method.
     * This controller serves the path "/user".
     * Accessible to any user and not dependent on access rights.
     *
     * @param userRequest The request containing user details.
     * @return UserResponse with user information and HTTP status CREATED.
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserResponse createUser(@Valid @RequestBody UserRequest userRequest) {
        return userService.createUser(userRequest);
    }

    /**
     * Delete a user by their name using HTTP DELETE method.
     * This controller serves the path "/user/{name}".
     * Accessible to administrators only.
     *
     * @param name The name of the user to be deleted.
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{name}")
    public void deleteUser(@PathVariable String name) {
        userService.deleteUser(name);
    }

    /**
     * Get a paginated list of users using HTTP GET method.
     * This controller serves the path "/user/get-page".
     * Accessible to administrators only.
     *
     * @param page The page number (default: 0(first page)).
     * @return List of UserResponse objects on the specified page.
     */
    @GetMapping("/get-page")
    public List<UserResponse> getUsersOnPage(@RequestParam(name = "page", defaultValue = "0") int page) {
        PageRequest pageRequest = PageRequest.of(page, 30);
        return userService.getAllUsersOnPage(pageRequest);
    }

    /**
     * Add a premium role to a user using HTTP POST method.
     * This controller serves the path "/user/{userId}".
     * Accessible to administrators only.
     *
     * @param userId The ID of the user to add the premium role to.
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{userId}")
    public void addRolePremium(@PathVariable Long userId) {
        userService.addPremiumRole(userId);
    }
}
