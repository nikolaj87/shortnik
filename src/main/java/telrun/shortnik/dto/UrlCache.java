package telrun.shortnik.dto;

import java.sql.Timestamp;
import java.util.Objects;

public class UrlCache {
    private long id;
    private String longUrl;
    private Timestamp lastUse;

    public UrlCache(long id, String longUrl, Timestamp lastUse) {
        this.id = id;
        this.longUrl = longUrl;
        this.lastUse = lastUse;
    }

    public UrlCache() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public Timestamp getLastUse() {
        return lastUse;
    }

    public void setLastUse(Timestamp lastUse) {
        this.lastUse = lastUse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UrlCache urlCache = (UrlCache) o;
        return id == urlCache.id && Objects.equals(longUrl, urlCache.longUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, longUrl);
    }

    @Override
    public String toString() {
        return "UrlCache{" +
                "id=" + id +
                ", longUrl='" + longUrl + '\'' +
                ", lastUse=" + lastUse +
                '}';
    }
}

