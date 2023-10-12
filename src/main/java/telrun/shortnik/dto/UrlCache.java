package telrun.shortnik.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.sql.Timestamp;
import java.util.Objects;

public class UrlCache {
    private long id;
    private String longUrl;
    private Timestamp lastUse;
    private Integer numberUsage;

    public UrlCache(long id, String longUrl, Timestamp lastUse, Integer numberUsage) {
        this.id = id;
        this.longUrl = longUrl;
        this.lastUse = lastUse;
        this.numberUsage = numberUsage;
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

    public Integer getNumberUsage() {
        return numberUsage;
    }

    public void setNumberUsage(Integer numberUsage) {
        this.numberUsage = numberUsage;
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
                ", numberUsage=" + numberUsage +
                '}';
    }
}

