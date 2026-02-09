package com.caue.encurtador_url.service;

import com.caue.encurtador_url.exception.ResourceNotFoundException;
import com.caue.encurtador_url.model.Url;
import com.caue.encurtador_url.repository.UrlRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UrlService {
    private final UrlRepository urlRepository;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    private String createShortUrl() {
        String shortUrl;
        do {
            shortUrl = RandomStringUtils.randomAlphanumeric(6);
        } while (urlRepository.findByShortUrl(shortUrl).isPresent());

        return shortUrl;
    }

    @Transactional(readOnly = true)
    public Url getOriginalUrl(String shortUrl) {
        Url url = urlRepository.findByShortUrl(shortUrl)
                .orElseThrow(() -> new ResourceNotFoundException("URL não encontrada: " + shortUrl));

        if (url.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new ResourceNotFoundException("URL expirada: " + shortUrl);
        }

        return url;
    }

    @Transactional
    public String shortenUrl(String originalUrl) {
        String shortUrl = createShortUrl();

        Url url = new Url();
        url.setOriginalUrl(originalUrl);
        url.setShortUrl(shortUrl);
        url.setExpirationDate(LocalDateTime.now().plusDays(30));

        urlRepository.save(url);

        return shortUrl;
    }
}