package telrun.shortnik.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import telrun.shortnik.convertors.Convertors;
import telrun.shortnik.dto.UserRequest;
import telrun.shortnik.dto.UserResponse;
import telrun.shortnik.entity.Role;
import telrun.shortnik.entity.User;
import telrun.shortnik.exception.ExceptionHandlers;
import telrun.shortnik.repository.UserRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * A service for working with the {@link User} class.
 */
@Service
public class UserServiceImpl implements UserService {
    /**
     * A standard Spring bean for password hashing.
     */
    private final PasswordEncoder passwordEncoder;
    /**
     * A repository for working with the {@link User} class.
     */
    private final UserRepository userRepository;
    /**
     * Converters necessary for transforming DTO, ENTITY, and vice versa.
     */
    private final Convertors convertors;
    /**
     * The default role for every new user in the system.
     */
    private static final Role USER_ROLE = new Role(3L, "ROLE_USER", null);
    /**
     * A conditional paid premium role in the application that allows having links without time and quantity restrictions,
     * and in the future, it will provide access to additional statistics data.
     */
    private static final Role PREMIUM_ROLE = new Role(2L, "ROLE_PREMIUM", null);
    private static final Logger LOGGER = LogManager.getLogger(ExceptionHandlers.class);
    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, Convertors convertors) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.convertors = convertors;
    }

    /**
     * A method for registering new users in the database.
     *
     * @param userRequest An object containing information about the user.
     * @return The saved user from the database with only the necessary fields.
     */
    @Override
    public UserResponse createUser(UserRequest userRequest) {
        User userForSave = new User(0L, userRequest.getName(), passwordEncoder.encode(userRequest.getPassword()),
                userRequest.getEmail(), new Timestamp(System.currentTimeMillis()), Set.of(USER_ROLE), Set.of());
        userForSave = userRepository.save(userForSave);
        LOGGER.info("new user join shortnikApp -" + userRequest);
        return convertors.entityToUserResponse(userForSave);
    }

    /**
     * Delete a user from the database.
     *
     * @param name The name of the user to be deleted.
     */
    @Override
    public void deleteUser(String name) {
        userRepository.deleteUserByName(name);
    }

    /**
     * A method for searching users in the database, including their roles and URLs associated with them.
     * Pagination is used.
     *
     * @param pageRequest Specifies the page (number of users and page number).
     * @return Returns a list of {@link UserResponse} on the specified page.
     */
    @Override
    public List<UserResponse> getAllUsersOnPage(PageRequest pageRequest) {
        Page<User> allUsersOnPage = userRepository.findAll(pageRequest);
        return allUsersOnPage.stream().map(convertors::entityToUserResponse).toList();
    }
    /**
     * Adds a premium role to a user identified by their user ID.
     *
     * @param userId The unique identifier of the user to whom the premium role is added.
     */
    @Override
    public void addPremiumRole(Long userId) {
        Optional<User> entityUserOptional = userRepository.findById(userId);
        if (entityUserOptional.isPresent()) {
            User entityUser = entityUserOptional.get();
            entityUser.getRoles().add(PREMIUM_ROLE);
            userRepository.save(entityUser);
        }
    }
    /**
     * Loads a user by their username.
     *
     * @param username The username of the user to be loaded.
     * @return The user with the specified username.
     * @throws UsernameNotFoundException if no user with the given username is found.
     */
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userByUsername = userRepository.findUserByName(username);
        if (userByUsername.isEmpty()) throw new UsernameNotFoundException("no such userName: " + username);
        return userByUsername.get();
    }

}
