package telrun.shortnik.cache;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import telrun.shortnik.controllers.api.GsoupHttpConnector;
import telrun.shortnik.controllers.api.JsonCreator;
import telrun.shortnik.dto.UrlCache;
import telrun.shortnik.dto.UrlRequest;
import telrun.shortnik.entity.User;
import telrun.shortnik.repository.UrlRepository;
import telrun.shortnik.repository.UserRepository;
import telrun.shortnik.service.UrlService;
import telrun.shortnik.service.UserService;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class UrlReadCacheManagerTest {

    private final UrlReadCacheManager urlReadCacheManager;
    private final UrlRepository urlRepository;
    private final UrlService urlService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final String testOriginalUrl = "https://www.google.com/sometesturlineed";

    @Autowired
    public UrlReadCacheManagerTest(UrlReadCacheManager urlReadCacheManager, JsonCreator jsonCreator, GsoupHttpConnector connector, UrlRepository urlRepository, UrlService urlService, UserRepository userRepository, UserService userService) {
        this.urlReadCacheManager = urlReadCacheManager;
        this.urlRepository = urlRepository;
        this.urlService = urlService;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @BeforeEach
    public void methodBefore() {
    }

    @AfterEach
    public void clearDb() {
    }

//    @Test
    void readData() throws InterruptedException {
        urlRepository.deleteAll();

        Optional<User> userOptional = userRepository.findUserByName("user");
        User user = userOptional.get();
        String shortUrl = "";

        for (int i = 0; i < 10; i++) {
            shortUrl = urlService.createUrl(new UrlRequest(testOriginalUrl + i, "", user), user);
            urlService.getLongUrlByShortName(shortUrl);
        }
        System.out.println(urlReadCacheManager.getCache()); //--------------------------
        System.out.println(urlReadCacheManager.getCache().size()); //--------------------------

        Timestamp lastUse = urlReadCacheManager.getCache().get(shortUrl).getLastUse();

        urlService.getLongUrlByShortName(shortUrl);
        Timestamp lastUseNewTime = urlReadCacheManager.getCache().get(shortUrl).getLastUse();

        shortUrl = urlService.createUrl(new UrlRequest(testOriginalUrl, "", user), user);
        urlService.getLongUrlByShortName(shortUrl);

        System.out.println(urlReadCacheManager.getCache());  // ----------------------------

    }


//    @Test
//    void afterAdd200UrlsCacheIsFull_afterCleanCacheItIsCleaned() throws InterruptedException {
//        AtomicReference<String> shortUrl = new AtomicReference<>("");
//
//        Thread writer = new Thread(() -> {
//            for (int i = 0; i < 200; i++) {
//                shortUrl.set(urlReadCacheManager.writeData(new UrlRequest(testOriginalUrl + i, "", testUser)));
//                try {
//                    Thread.sleep(new Random().nextInt(100));
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        });
//        Thread reader = new Thread(() -> {
//            while (!Thread.interrupted()) {
//                urlReadCacheManager.readData(shortUrl.get());
//                try {
//                    Thread.sleep(new Random().nextInt(2));
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                }
//            }
//        });
//        writer.start();
//        reader.start();
//        writer.join();
//
//        reader.interrupt();
//        reader.join();
//
//        int sizeResult = urlReadCacheManager.getCache().size();
//        urlReadCacheManager.cleanCache();
//
//        Assertions.assertEquals(UrlReadCacheManager.MAX_CACHE_SIZE, sizeResult);
//        Assertions.assertTrue(urlReadCacheManager.getCache().size() < sizeResult);
//    }


    void deleteData() {
        for (int i = 0; i < 10; i++) {
            urlReadCacheManager.putInCache("1" + i, new UrlCache(1L, "123" + i, new Timestamp(System.currentTimeMillis())));
        }
        System.out.println(urlReadCacheManager.getCache());
        System.out.println(urlReadCacheManager.getCache().size());

        List<UrlCache> urlCaches = urlReadCacheManager.cleanCache();
        System.out.println(urlReadCacheManager.getCache());
    }
}