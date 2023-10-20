package telrun.shortnik.controllers.api;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
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

    @PostMapping
    public ResponseEntity<String> createUrl (@Valid @RequestBody UrlRequest urlRequest, @AuthenticationPrincipal User user) {
        urlRequest.setUser(user);
        String shortUrl = urlService.createUrl(urlRequest, user);
        return new ResponseEntity<>(shortUrl, HttpStatus.CREATED);
    }

    @GetMapping("/{urlShort}")
    public UrlResponse redirectToLongUrl (@PathVariable String urlShort) {
        String originalUrl = urlService.getLongUrlByShortName(urlShort);
        return new UrlResponse(originalUrl);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/delete/{shortUrl}")
    public void deleteUrl (@PathVariable String shortUrl) {
        urlService.deleteUrl(shortUrl);
    }
}

