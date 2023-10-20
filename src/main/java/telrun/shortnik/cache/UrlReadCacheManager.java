package telrun.shortnik.cache;

import org.springframework.stereotype.Component;
import telrun.shortnik.dto.UrlCache;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UrlReadCacheManager {

    private final ConcurrentHashMap<String, UrlCache> cache = new ConcurrentHashMap<>();
    public static final int MAX_CACHE_SIZE = 10;
    private static final long LIFE_TIME = 10_000; //1 minutes

    public static int getCacheMaxSize() {
        return MAX_CACHE_SIZE;
    }

    public static long getLifeTime() {
        return LIFE_TIME;
    }

    public ConcurrentHashMap<String, UrlCache> getCache() {
        return cache;
    }

    public int getCacheCurrentSize() {
        return cache.size();
    }

    public void putInCache(String key, UrlCache value) {
        cache.put(key, value);
    }

    public UrlCache findInCache(String key) {
        return cache.get(key);
    }

    public List<UrlCache> cleanCache() {
        List<UrlCache> listForUpdate = new LinkedList<>();

        Iterator<Map.Entry<String, UrlCache>> iterator = cache.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, UrlCache> entry = iterator.next();
            if (System.currentTimeMillis() - entry.getValue().getLastUse().getTime() > LIFE_TIME) {
                listForUpdate.add(entry.getValue());
                iterator.remove();
            }
        }
        return listForUpdate;
    }
}

//        UrlCache urlCache = cache.computeIfPresent(urlShort, (key, value) -> new UrlCache(value.getId(), value.getLongUrl(),
//                new Timestamp(System.currentTimeMillis())));
//
//        if (urlCache != null) {
//            return urlCache.getLongUrl();
//        }
//        return null;

//            cache.replaceAll((key, value) -> {
//
//                if (((System.currentTimeMillis() - value.getLastUse().getTime()) > MIN_TIME_USAGE)
//                        || (value.getNumberUsage() < MIN_NUMBER_USAGE_CACHE)) {
//                    return value;
//                } else {
//                    return null;
//                }
//            });


