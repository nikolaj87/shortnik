package telrun.shortnik.dto;


import java.util.Objects;

public class UrlResponse {
    private String longUrl;

    public UrlResponse(String longUrl) {
        this.longUrl = longUrl;
    }

    public UrlResponse() {
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UrlResponse that = (UrlResponse) o;
        return Objects.equals(longUrl, that.longUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(longUrl);
    }

    @Override
    public String toString() {
        return "UrlResponse{" +
                "longUrl='" + longUrl + '\'' +
                '}';
    }
}
