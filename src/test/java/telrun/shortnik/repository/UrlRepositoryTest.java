package telrun.shortnik.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import telrun.shortnik.entity.Url;
import telrun.shortnik.entity.User;
import telrun.shortnik.service.UrlService;

import java.sql.Timestamp;

@SpringBootTest
class UrlRepositoryTest {

    private final UrlRepository urlRepository;
    private final UrlService urlService;
    private final UserRepository userRepository;

    @Autowired
    UrlRepositoryTest(UrlRepository urlRepository, UrlService urlService, UserRepository userRepository) {
        this.urlRepository = urlRepository;
        this.urlService = urlService;
        this.userRepository = userRepository;
    }


    void updateUrlById() {
        urlService.deleteUrl("777777");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        User user = userRepository.findUserByName("user").get();
        Url testUrl = new Url(0L, "777777", "some original url", timestamp, timestamp, "", user);
        testUrl = urlRepository.save(testUrl);

        urlRepository.updateUrlById(testUrl.getId(), new Timestamp(System.currentTimeMillis()));
        Timestamp lastUse = urlRepository.findById(testUrl.getId()).get().getLastUse();

        Assertions.assertNotEquals(timestamp, lastUse);
    }

    //    @Test
    void deeteOutdatedUrls() {
    }

    //    @Test
    void countAllByUser() {
    }
}