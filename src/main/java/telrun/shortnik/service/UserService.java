package telrun.shortnik.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;
import telrun.shortnik.dto.UserRequest;
import telrun.shortnik.dto.UserResponse;

import java.util.List;

public interface UserService extends UserDetailsService {
    @Transactional
    UserResponse createUser(UserRequest userRequest);
    @Transactional
    void deleteUser(String name);
    @Transactional(readOnly = true)
    List<UserResponse> getAllUsersOnPage(PageRequest pageRequest);
    @Transactional
    void addPremiumRole(Long userId);
}
