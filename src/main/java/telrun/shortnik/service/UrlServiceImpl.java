package telrun.shortnik.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;
import telrun.shortnik.dto.UrlRequest;
import telrun.shortnik.entity.Url;
import telrun.shortnik.entity.User;
import telrun.shortnik.generator.Generator;
import telrun.shortnik.repository.UrlRepository;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.Optional;

@Service
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;
    private final Generator generator;

    @Autowired
    public UrlServiceImpl(UrlRepository urlRepository, Generator generator) {
        this.urlRepository = urlRepository;
        this.generator = generator;
    }

    @Override
    public ResponseEntity<String> createUrl(UrlRequest urlRequest) {

        Optional<Url> optionalUrlFromDb = urlRepository.findByLongUrl(urlRequest.getLongUrl());
        if (optionalUrlFromDb.isPresent()) {
            return new ResponseEntity<>(optionalUrlFromDb.get().getShortUrl(), HttpStatus.CREATED);
        }
        Url urlForSave = new Url(0L, generator.generate(), urlRequest.getLongUrl(), new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis()), urlRequest.getDescription(), urlRequest.getUser());
        urlRepository.save(urlForSave);
        return new ResponseEntity<>(urlForSave.getShortUrl(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<String> getShortUrlByLongName(String url) {
        return null;
    }

    @Override
    public RedirectView getLongUrlByShorName(String urlShort) {
        Optional<Url> foundOriginalUrl = urlRepository.findByShortUrl(urlShort);
        if (foundOriginalUrl.isPresent()) {
            return new RedirectView(foundOriginalUrl.get().getLongUrl());
        }
        return new RedirectView("/");
    }

    @Override
    public void deleteUrl(String shortUrl) {
        urlRepository.deleteUrlByShortUrl(shortUrl);
    }
}

