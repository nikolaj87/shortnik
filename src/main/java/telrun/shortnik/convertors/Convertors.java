package telrun.shortnik.convertors;

import org.springframework.stereotype.Component;
import telrun.shortnik.dto.UrlRequest;
import telrun.shortnik.dto.UserResponse;
import telrun.shortnik.entity.Url;
import telrun.shortnik.entity.User;

import java.sql.Timestamp;

@Component
public class Convertors {
    public Url requestToEntityUrl(UrlRequest urlRequest) {
        return new Url(0L, "", urlRequest.getLongUrl(), new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis()), urlRequest.getDescription(), urlRequest.getUser());
    }
    public UserResponse entityToUserResponse(User userEntity) {
        return new UserResponse(userEntity.getId(), userEntity.getName(), userEntity.getEmail(),
                userEntity.getRegisteredAt(), userEntity.getRoles(), userEntity.getUrls());
    }
}
