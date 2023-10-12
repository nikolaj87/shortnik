package telrun.shortnik.service.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import telrun.shortnik.convertors.Convertors;
import telrun.shortnik.dto.UrlCache;
import telrun.shortnik.dto.UrlRequest;
import telrun.shortnik.entity.Url;
import telrun.shortnik.repository.UrlRepository;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


@Component
@EnableScheduling
@EnableAsync
public class UrlReadCacheManager {

    private final UrlRepository urlRepository;
    private final Convertors convertors;
    private final ConcurrentHashMap<String, UrlCache> cache = new ConcurrentHashMap<>();
    public   static final int MAX_CACHE_SIZE = 100;
    private  static final int MIN_NUMBER_USAGE_CACHE = 100;
    private  static final long MIN_TIME_USAGE = 180_000; //3 minutes

    @Autowired
    public UrlReadCacheManager(UrlRepository urlRepository, Convertors convertors) {
        this.urlRepository = urlRepository;
        this.convertors = convertors;
    }

    public Map<String, UrlCache> getCache() {
        return cache;
    }

    public String readData(String urlShort) {
        //проверю есть ли в кешэ строка
        UrlCache urlCache = cache.computeIfPresent(urlShort, (key, value) -> new UrlCache(value.getId(), value.getLongUrl(),
                new Timestamp(System.currentTimeMillis()), value.getNumberUsage() + 1));
        //и если есть я не пойду в бд и верну адрес переадрусации
        if (urlCache != null) {
            return urlCache.getLongUrl();
        }
        //если же в кешэ нет то пойду и гляну в бд
        Optional<Url> entityUrlOptional = urlRepository.findByShortUrl(urlShort);
        if (entityUrlOptional.isPresent()) {
            Url entityUrl = entityUrlOptional.get();
            entityUrl.getLastUse().setTime(System.currentTimeMillis());
            //если в бд есть то закину это в кеш если он не переполнен
            if (cache.size() < MAX_CACHE_SIZE) {
                cache.put(urlShort, new UrlCache(entityUrl.getId(), entityUrl.getLongUrl(),
                        new Timestamp(System.currentTimeMillis()), 1));
            }
            return entityUrl.getLongUrl();
        }
        //если не нашлось и в бд верну обратно на страницу
        return "/";
    }

    //транзакционность тут ненужна я же не работаю с бд? только с кешом???
    @Scheduled(cron = "0 */10 * * * *")
    public void cleanCache() {

        if (cache.size() >= MAX_CACHE_SIZE) {
            cache.entrySet().removeIf(entry -> entry.getValue().getNumberUsage() < MIN_NUMBER_USAGE_CACHE ||
                    entry.getValue().getLastUse().getTime() < MIN_TIME_USAGE);

//            cache.replaceAll((key, value) -> {
//
//                if (((System.currentTimeMillis() - value.getLastUse().getTime()) > MIN_TIME_USAGE)
//                        || (value.getNumberUsage() < MIN_NUMBER_USAGE_CACHE)) {
//                    return value;
//                } else {
//                    return null;
//                }
//            });
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

