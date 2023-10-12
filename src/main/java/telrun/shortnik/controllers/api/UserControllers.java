package telrun.shortnik.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    public UserResponse createUser(@RequestBody UserRequest userRequest) {
        return userService.createUser(userRequest);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{name}")
    public void deleteUser(@PathVariable String name) {
        userService.deleteUser(name);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<UserResponse> getAllUsers() {
        PageRequest page = PageRequest.of(0, 100);

        return userService.getAllUsers();
    }

//    @GetMapping(params = "recent")
//    public Iterable<Taco> recentTacos() {
//        PageRequest page = PageRequest.of(0, 2, Sort.by("createdAt").descending());
//        return tacoRepo.findAll(page).getContent();
//    }
//
//    @GetMapping()
//    public Iterable<Taco> getAllTacos() {
//        PageRequest page = PageRequest.of(0, 3, Sort.by("createdAt").descending());
//        return tacoRepo.findAll(page).getContent();
//    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{userId}")
    public void addRolePremium(@PathVariable Long userId) {
        userService.addPremiumRole(userId);
    }
}
