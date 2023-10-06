package telrun.shortnik.controllers.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import telrun.shortnik.dto.UrlRequest;
import telrun.shortnik.service.UrlService;

@RestController
@RequestMapping
public class UrlControllers {

    private final UrlService urlService;

    public UrlControllers(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/url")
    public ResponseEntity<String> createUrl (@RequestBody UrlRequest urlRequest) {
        return urlService.createUrl(urlRequest);
    }

    @GetMapping("url/{originalUrl}")
    public ResponseEntity<String> getShortLinkByLongName (@PathVariable String originalUrl) {
        return urlService.getShortUrlByLongName(originalUrl);
    }

    @GetMapping("/{urlShort}")
    public RedirectView redirectToLongUrl (@PathVariable String urlShort) {
        return urlService.getLongUrlByShorName(urlShort);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/delete/{shortUrl}")
    public void deleteUrl (@PathVariable String shortUrl) {
        //путь ссылки дожен быть правильно прочтен спрингом
        //он может путь ссыку и путь к контроллеру
        urlService.deleteUrl(shortUrl);
    }
}

