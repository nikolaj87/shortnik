package telrun.shortnik.dto;

import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;
import telrun.shortnik.entity.User;

import java.util.Objects;

public class UrlRequest {
    @URL
    @Size(min = 30, max = 2048)
    private String longUrl;
    @Size(max = 255)
    private String description;

    private User user;

    public UrlRequest(String longUrl, String description, User user) {
        this.longUrl = longUrl;
        this.description = description;
        this.user = user;
    }

    public UrlRequest() {
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
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
        UrlRequest that = (UrlRequest) o;
        return Objects.equals(longUrl, that.longUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(longUrl);
    }
}
