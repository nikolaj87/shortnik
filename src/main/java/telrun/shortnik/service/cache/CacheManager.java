package telrun.shortnik.service.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import telrun.shortnik.convertors.Convertors;
import telrun.shortnik.dto.UrlRequest;
import telrun.shortnik.entity.Url;
import telrun.shortnik.repository.UrlRepository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


@Component
@EnableScheduling
@EnableAsync
public class CacheManager {

    private final UrlRepository urlRepository;
    private final Convertors convertors;
    private final Map<String, Url> cache = new ConcurrentHashMap<>();

    @Autowired
    public CacheManager(UrlRepository urlRepository, Convertors convertors) {
        this.urlRepository = urlRepository;
        this.convertors = convertors;
    }

    public String readData(String urlShort) {
        if (cache.containsKey(urlShort)) {
            return cache.get(urlShort).getLongUrl();
        }
        Optional<Url> entityUrlOptional = urlRepository.findByShortUrl(urlShort);
        if (entityUrlOptional.isPresent()) {
            Url entityUrl = entityUrlOptional.get();
            entityUrl.getLastUse().setTime(System.currentTimeMillis());  // обновляю lastUse
            cache.put(urlShort, entityUrl);
            return entityUrl.getLongUrl();
        }
        return "/";
    }

    //транзакционность тут ненужна я же не работаю с бд? только с кешом???
    @Scheduled(cron = "0 0,10,20,30,40,50 * * * *")
    public void cleanCache() {
        if (cache.size() > 100) {
            cache.replaceAll((key, value) -> {
                if (System.currentTimeMillis() - value.getLastUse().getTime() < 100_000) {
                    return null;
                } else {
                    return value;
                }
            });
        }
    }

    public String writeData(UrlRequest urlRequest) {

        Optional<Url> optionalUrlFromDb = urlRepository.findByLongUrl(urlRequest.getLongUrl());
        if (optionalUrlFromDb.isPresent()) {
            return optionalUrlFromDb.get().getShortUrl();
        }

        Url urlEntityForSave = convertors.requestToEntityUrl(urlRequest);
        urlEntityForSave = urlRepository.save(urlEntityForSave);

        return urlEntityForSave.getShortUrl();
    }

    public void deleteData(String shortUrl) {
        urlRepository.deleteUrlByShortUrl(shortUrl);
        cache.remove(shortUrl);
    }
}

