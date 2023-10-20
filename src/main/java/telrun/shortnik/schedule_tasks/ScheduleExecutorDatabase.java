package telrun.shortnik.schedule_tasks;

import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import telrun.shortnik.service.UrlService;

@Component
@EnableScheduling
@EnableAsync
public class ScheduleExecutorDatabase {

    private final UrlService urlService;

    public ScheduleExecutorDatabase(UrlService urlService) {
        this.urlService = urlService;
    }

    @Scheduled(cron = "0 0 02 * * *")
    public void cronExpressionTask() {
        urlService.cleanDatabase();
    }
}