package telrun.shortnik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import telrun.shortnik.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByName(String name);

    void deleteUserByName(String name);

    //    @Query(value = "UPDATE User u SET u.roles = :newRoles WHERE u.id = :userId", nativeQuery = true)
    @Modifying
    @Query(value = "INSERT INTO role_user (user_id, role_id) VALUES (:userId, 2)", nativeQuery = true)
    void updateRoles(@Param("userId") Long userId);

}
