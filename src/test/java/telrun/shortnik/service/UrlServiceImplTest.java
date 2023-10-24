package telrun.shortnik.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import telrun.shortnik.convertors.Convertors;
import telrun.shortnik.dto.UrlRequest;
import telrun.shortnik.entity.User;
import telrun.shortnik.exception.NoPremiumRoleException;
import telrun.shortnik.generator.Generator;
import telrun.shortnik.repository.UrlRepository;
import telrun.shortnik.repository.UserRepository;


@SpringBootTest
class UrlServiceImplTest {

    private final UrlRepository mockRepository = Mockito.mock();
    private final UserRepository userRepository;
    private final UrlServiceImpl urlServiceImpl = new UrlServiceImpl(mockRepository, new Convertors(), new Generator());

    @Autowired
    UrlServiceImplTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    void mustThrowNoPremiumRoleException() {
        User user = userRepository.findUserByName("user").get();
        Mockito.when(mockRepository.countAllByUser(user)).thenReturn(30);

        Assertions.assertThrows(NoPremiumRoleException.class, ()->
            urlServiceImpl.createUrl(new UrlRequest(), user));
    }
    @Test
    void mustReturnTheSamePage() {
        String notExistingRequest = "this url is not exist";

        String resultOfBadRequest = urlServiceImpl.getLongUrlByShortName(notExistingRequest);

        Assertions.assertEquals("/", resultOfBadRequest);
    }
}
