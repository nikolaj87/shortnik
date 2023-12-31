package telrun.shortnik.dto;

import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;
import telrun.shortnik.entity.User;

import java.util.Objects;
/**
 * UrlRequest class represents a request containing URL information, including the long URL, description, and the associated user.
 */
public class UrlRequest {
    /**
     * The long URL to be shortened. It must be a valid URL, with a minimum length of 30 characters and a maximum length of 2048 characters.

     * URL(message = "please enter a valid url") Specifies that the field must be a valid URL.
     * Size(min = 30, max = 2048, message = "url should be between 30 and 2048 characters") Specifies the size constraints for the field, including the minimum and maximum lengths.
     */
    @URL(message = "please enter a valid url")
    @Size(min = 30, max = 2048, message = "url should be between 30 and 2048 characters")
    private String longUrl;

    @Size(max = 255, message = "description should be between 0 and 255 characters")
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
