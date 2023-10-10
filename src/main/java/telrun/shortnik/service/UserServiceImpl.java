package telrun.shortnik.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import telrun.shortnik.convertors.Convertors;
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
    private final Convertors convertors;
    private static final Role DEFAULT_ROLE = new Role(3L, "USER", null);

        @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, Convertors convertors) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.convertors = convertors;
    }

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        User userForSave = new User(0L, userRequest.getName(), passwordEncoder.encode(userRequest.getPassword()),
                userRequest.getEmail(), new Timestamp(System.currentTimeMillis()), Set.of(DEFAULT_ROLE), Set.of());
        userForSave = userRepository.save(userForSave);
        return convertors.entityToUserResponse(userForSave);
    }

    @Override
    public void deleteUser(String name) {
        userRepository.deleteUserByName(name);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        return allUsers.stream().map(convertors::entityToUserResponse).toList();
    }

    @Override
    public void addPremiumRole(Long userId) {
        userRepository.updateRoles(userId);
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userByUsername = userRepository.findUserByName(username);
        if (userByUsername.isEmpty()) throw new UsernameNotFoundException("no such userName: " + username);
        return userByUsername.get();
    }

}
