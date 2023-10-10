package telrun.shortnik.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "short_url")
    private String shortUrl;
    @Column(name = "long_url")
    private String longUrl;
    @Column(name = "created_at")
    private Timestamp createdAt;
    @Column(name = "last_use")
    private Timestamp lastUse;
    @Column(name = "description")
    private String description;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Url(long id, String shortUrl, String longUrl, Timestamp createdAt, Timestamp lastUse, String description, User user) {
        this.id = id;
        this.shortUrl = shortUrl;
        this.longUrl = longUrl;
        this.createdAt = createdAt;
        this.lastUse = lastUse;
        this.description = description;
        this.user = user;
    }

    public Url() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortName) {
        this.shortUrl = shortName;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longName) {
        this.longUrl = longName;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getLastUse() {
        return lastUse;
    }

    public void setLastUse(Timestamp lastUse) {
        this.lastUse = lastUse;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Url url = (Url) o;
        return id == url.id && Objects.equals(longUrl, url.longUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, longUrl);
    }

    @Override
    public String toString() {
        return "Url{" +
                "id=" + id +
                ", shortName='" + shortUrl + '\'' +
                ", longName='" + longUrl + '\'' +
                ", createdAt=" + createdAt +
                ", lastUse=" + lastUse +
                ", description='" + description + '\'' +
                ", user=" + user +
                '}';
    }
}
