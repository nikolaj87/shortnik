package telrun.shortnik.convertors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telrun.shortnik.dto.UrlRequest;
import telrun.shortnik.dto.UserResponse;
import telrun.shortnik.entity.Url;
import telrun.shortnik.entity.User;
import telrun.shortnik.generator.Generator;

import java.sql.Timestamp;

@Component
public class Convertors {

    private final Generator generator;

    @Autowired
    public Convertors(Generator generator) {
        this.generator = generator;
    }

    public Url requestToEntityUrl(UrlRequest urlRequest) {
        return new Url(0L, generator.generate(), urlRequest.getLongUrl(), new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis()), urlRequest.getDescription(), urlRequest.getUser());
    }
    public UserResponse entityToUserResponse(User userEntity) {
        return new UserResponse(userEntity.getId(), userEntity.getName(), userEntity.getEmail(),
                userEntity.getRegisteredAt(), userEntity.getRoles(), userEntity.getUrls());
    }
}
