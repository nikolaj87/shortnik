package telrun.shortnik.controllers.api;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import telrun.shortnik.dto.UrlRequest;
import telrun.shortnik.dto.UrlResponse;
import telrun.shortnik.entity.User;
import telrun.shortnik.service.UrlService;

@RestController
@RequestMapping("url")
public class UrlControllers {

    private final UrlService urlService;

    public UrlControllers(UrlService urlService) {
        this.urlService = urlService;
    }

    /**
     * Create a new URL and return the short URL.
     * This controller serves the path "/url".
     * Accessible to any user and not dependent on access rights.
     *
     * @param urlRequest The request containing URL details.
     * @param user       The authenticated user.
     * @return ResponseEntity with the short URL and HTTP status CREATED.
     */
    @PostMapping
    public ResponseEntity<String> createUrl(@Valid @RequestBody UrlRequest urlRequest, @AuthenticationPrincipal User user) {
        urlRequest.setUser(user);
        String shortUrl = urlService.createUrl(urlRequest, user);
        return new ResponseEntity<>(shortUrl, HttpStatus.CREATED);
    }

    /**
     * Redirect to the original long URL using the short URL.
     * This controller serves the path "/url/{urlShort}".
     * Accessible to any user and not dependent on access rights.
     *
     * @param urlShort The short URL.
     * @return A UrlResponse object with the original long URL.
     */
    @GetMapping("/{urlShort}")
    public UrlResponse redirectToLongUrl(@PathVariable String urlShort) {
        String originalUrl = urlService.getLongUrlByShortName(urlShort);
        return new UrlResponse(originalUrl);
    }

    /**
     * Delete a URL by its short URL.
     * This controller serves the path "/url/delete/{shortUrl}".
     * Accessible to administrators only.
     *
     * @param shortUrl The short URL of the URL to be deleted.
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/delete/{shortUrl}")
    public void deleteUrl(@PathVariable String shortUrl) {
        urlService.deleteUrl(shortUrl);
    }
}

