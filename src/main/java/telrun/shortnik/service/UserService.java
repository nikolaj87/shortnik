package telrun.shortnik.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;
import telrun.shortnik.dto.UserRequest;
import telrun.shortnik.dto.UserResponse;
import telrun.shortnik.entity.User;

import java.util.List;

public interface UserService extends UserDetailsService {

    @Transactional
    UserResponse createUser(UserRequest userRequest);

    @Transactional
    void deleteUser(String name);

    @Transactional(readOnly = true)
    List<UserResponse> getAllUsers();
}
