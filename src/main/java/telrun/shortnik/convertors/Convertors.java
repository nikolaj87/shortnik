package telrun.shortnik.convertors;

import org.springframework.stereotype.Component;
import telrun.shortnik.dto.UrlRequest;
import telrun.shortnik.dto.UserResponse;
import telrun.shortnik.entity.Url;
import telrun.shortnik.entity.User;

import java.sql.Timestamp;

@Component
public class Convertors {
    /**
     * Convert a URL request to a URL entity.
     *
     * @param urlRequest The URL request to be converted.
     * @return A new URL entity.
     */
    public Url requestToEntityUrl(UrlRequest urlRequest) {
        return new Url(0L, "", urlRequest.getLongUrl(), new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis()), urlRequest.getDescription(), urlRequest.getUser());
    }
    /**
     * Convert a User entity to a UserResponse.
     *
     * @param userEntity The User entity to be converted.
     * @return A UserResponse object.
     */
    public UserResponse entityToUserResponse(User userEntity) {
        return new UserResponse(userEntity.getId(), userEntity.getName(), userEntity.getEmail(),
                userEntity.getRegisteredAt(), userEntity.getRoles(), userEntity.getUrls());
    }
}
