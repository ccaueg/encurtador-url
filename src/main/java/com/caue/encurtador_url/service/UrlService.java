package com.caue.encurtador_url.service;

import com.caue.encurtador_url.exception.ResourceNotFoundException;
import com.caue.encurtador_url.model.Url;
import com.caue.encurtador_url.repository.UrlRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class UrlService {
    private final UrlRepository urlRepository;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    private String createShortUrl() {
        return RandomStringUtils.randomAlphanumeric(5, 10);
    }

    public Url getOriginalUrl(String shortUrl) {
        Url url = urlRepository.findByShortUrl(shortUrl)
                .orElseThrow(
                        () -> new ResourceNotFoundException("URL não encontrada para o código: " + shortUrl)
                );

        if (url.getExpirationDate().isBefore(LocalDateTime.now())) {
            urlRepository.delete(url);
        }

        return url;
    }

    public String shortenUrl(String originalUrl) {
        log.info("Encurtando URL: {}", originalUrl);

        try {
            String shortUrl = createShortUrl();
            log.debug("URL encurtada gerada: {}", shortUrl);

            Url url = new Url();
            url.setOriginalUrl(originalUrl);
            url.setShortUrl(shortUrl);
            url.setExpirationDate(LocalDateTime.now().plusDays(30));
            urlRepository.save(url);

            log.info("URL salva com sucesso: {} -> {}", originalUrl, shortUrl);

            return shortUrl;
        } catch (Exception exc) {
            log.error("Erro ao encurtar URL: {}", originalUrl, exc);
            throw exc;
        }
    }
}
