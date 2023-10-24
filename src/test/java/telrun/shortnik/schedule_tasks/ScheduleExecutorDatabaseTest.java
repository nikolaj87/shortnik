package telrun.shortnik.schedule_tasks;

import jakarta.validation.constraints.AssertTrue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import telrun.shortnik.entity.Url;
import telrun.shortnik.entity.User;
import telrun.shortnik.repository.UrlRepository;
import telrun.shortnik.repository.UserRepository;

import java.sql.Timestamp;
import java.util.Optional;

@SpringBootTest
class ScheduleExecutorDatabaseTest {

    private final ScheduleExecutorDatabase scheduleExecutorDatabase;
    private final UrlRepository urlRepository;
    private final UserRepository userRepository;

    @Autowired
    ScheduleExecutorDatabaseTest(ScheduleExecutorDatabase scheduleExecutorDatabase, UrlRepository urlRepository, UserRepository userRepository) {
        this.scheduleExecutorDatabase = scheduleExecutorDatabase;
        this.urlRepository = urlRepository;
        this.userRepository = userRepository;
    }

    @Test
    void mustDeleteOneUrlForNoPremiumUser() {
        Timestamp todayMinus33days = new Timestamp(System.currentTimeMillis() - (32L * 24L * 60L * 60L * 1000L));
        Timestamp today = new Timestamp(System.currentTimeMillis());
        User premium = userRepository.findUserByName("premium").get();
        User user = userRepository.findUserByName("user").get();
        Url forPremiumUser = new Url(0L, "111111", "longUrl11111", todayMinus33days, null, "", premium);
        Url noPremium = new Url(0L, "222222", "longUrl222222", todayMinus33days, null, "", user);
        Url noPremium2 = new Url(0L, "333333", "longUrl333333", today, null, "", user);
        urlRepository.save(forPremiumUser);
        urlRepository.save(noPremium);
        urlRepository.save(noPremium2);

        scheduleExecutorDatabase.cronExpressionTaskCleanDb();
        Optional<Url> createResultPremium = urlRepository.findByShortUrl(forPremiumUser.getShortUrl());
        Optional<Url> createResultNoPremium = urlRepository.findByShortUrl(noPremium.getShortUrl());
        Optional<Url> createResultNoPremium2 = urlRepository.findByShortUrl(noPremium2.getShortUrl());
        urlRepository.deleteAll();


        Assertions.assertTrue(createResultPremium.isPresent());
        Assertions.assertFalse(createResultNoPremium.isPresent());
        Assertions.assertTrue(createResultNoPremium2.isPresent());
    }
}