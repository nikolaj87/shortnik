package telrun.shortnik.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import telrun.shortnik.entity.Role;
import telrun.shortnik.entity.User;
import telrun.shortnik.repository.UserRepository;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    private static final Role ROLE_USER = new Role(3L, "USER", null);

    @Test
    void mustAddPremiumRole() {
        User testUser = new User(0L, "test", "test", "test@test", null, Set.of(ROLE_USER), null);
        testUser = userRepository.save(testUser);

        userService.addPremiumRole(testUser.getId());
        Optional<User> userWithPremiumRole = userRepository.findUserByName(testUser.getName());
        Set<Role> roles = userWithPremiumRole.get().getRoles();
        userService.deleteUser("test");

        assertEquals(2, roles.size());
    }

    @Test
    void mustThrowException() {
        assertThrows(UsernameNotFoundException.class, ()-> userService.loadUserByUsername("no Such Name In Database"));
    }
}