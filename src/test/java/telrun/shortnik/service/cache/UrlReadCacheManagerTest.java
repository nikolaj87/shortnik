package telrun.shortnik.service.cache;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import telrun.shortnik.controllers.api.GsoupHttpConnector;
import telrun.shortnik.controllers.api.JsonCreator;
import telrun.shortnik.dto.UrlRequest;
import telrun.shortnik.entity.Role;
import telrun.shortnik.entity.User;
import telrun.shortnik.repository.UrlRepository;
import telrun.shortnik.repository.UserRepository;

import java.sql.Timestamp;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class UrlReadCacheManagerTest {

    private final UrlReadCacheManager urlReadCacheManager;
    private final UrlRepository urlRepository;
    private final UserRepository userRepository;
    private final String testOriginalUrl = "https://www.google.com/sometesturlineed";
    private User testUser;
//    private static String srt = "";

    @Autowired
    public UrlReadCacheManagerTest(UrlReadCacheManager urlReadCacheManager, JsonCreator jsonCreator, GsoupHttpConnector connector, UrlRepository urlRepository, UserRepository userRepository) {
        this.urlReadCacheManager = urlReadCacheManager;
        this.urlRepository = urlRepository;
        this.userRepository = userRepository;
    }

    @BeforeEach
    public void methodBefore() {

        urlRepository.deleteAll();
        userRepository.deleteAll();
        testUser = userRepository.save(new User(0L, "testUser", "testPassword", "testEmail",
                new Timestamp(System.currentTimeMillis()), Set.of(new Role(3L, "USER", null)), Set.of()));
        testUser.setRegisteredAt(null);
    }

    @AfterEach
    public void clearDb() {
        urlRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void readData() {
    }

    @Test
    void cleanCache() {
    }

    @Test
    void afterAdd200UrlsCacheIsFull_afterCleanCacheItIsCleaned() throws InterruptedException {
        AtomicReference<String> shortUrl = new AtomicReference<>("");

        Thread writer = new Thread(() -> {
            for (int i = 0; i < 200; i++) {
                shortUrl.set(urlReadCacheManager.writeData(new UrlRequest(testOriginalUrl + i, "", testUser)));
                System.out.println(shortUrl);
                try {
                    Thread.sleep(new Random().nextInt(100));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Thread reader = new Thread(() -> {
            while (!Thread.interrupted()) {
                urlReadCacheManager.readData(shortUrl.get());
                try {
                    Thread.sleep(new Random().nextInt(2));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        writer.start();
        reader.start();
        writer.join();

        reader.interrupt();
        reader.join();

        int sizeResult = urlReadCacheManager.getCache().size();
        urlReadCacheManager.cleanCache();

        Assertions.assertEquals(UrlReadCacheManager.MAX_CACHE_SIZE, sizeResult);
        Assertions.assertTrue(urlReadCacheManager.getCache().size() < sizeResult);
    }

    @Test
    void deleteData() {
    }
}