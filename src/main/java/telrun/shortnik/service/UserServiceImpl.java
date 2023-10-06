package telrun.shortnik.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.RedirectView;
import telrun.shortnik.dto.UserRequest;
import telrun.shortnik.dto.UserResponse;
import telrun.shortnik.entity.Role;
import telrun.shortnik.entity.User;
import telrun.shortnik.repository.UserRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final Role defaultRole = new Role(3L, "USER", null);

    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public void createUser(UserRequest userRequest) {
        User userForSave = new User(0L, userRequest.getName(), passwordEncoder.encode(userRequest.getPassword()),
                userRequest.getEmail(), new Timestamp(System.currentTimeMillis()), Set.of(defaultRole));
        userRepository.save(userForSave);
    }

    @Override
    public void deleteUser(String name) {
        userRepository.deleteUserByName(name);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        return allUsers.stream().map(user -> new UserResponse(user.getId(), user.getName(),
                user.getEmail(), user.getRegisteredAt(), user.getRoles())).toList();
    }

    @Transactional(readOnly = true)  //???? надо ли ведь в интерфейсе нету аннотации
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userByUsername = userRepository.findUserByName(username);
        if (userByUsername.isEmpty()) throw new UsernameNotFoundException("no such userName: " + username);
        return userByUsername.get();
    }

    public boolean authenticateUser(String name, String password, HttpSession session) {
        User userForAuthenticate = loadUserByUsername(name);
        if (passwordEncoder.matches(password, userForAuthenticate.getPassword())) {
            Authentication authentication = new UsernamePasswordAuthenticationToken(userForAuthenticate,
                    null, userForAuthenticate.getAuthorities());
            session.setAttribute("userId", userForAuthenticate.getId());
            return true;
        }
        return false;
    }
}
