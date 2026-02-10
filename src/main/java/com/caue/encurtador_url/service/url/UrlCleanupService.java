package com.caue.encurtador_url.service.url;

import com.caue.encurtador_url.model.Url;
import com.caue.encurtador_url.repository.UrlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UrlCleanupService {

    private static final Logger logger = LoggerFactory.getLogger(UrlCleanupService.class);
    private final UrlRepository urlRepository;

    public UrlCleanupService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Transactional
    public void cleanupExpiredUrls() {
        List<Url> expiredUrls = urlRepository.findByExpirationDateBefore(LocalDateTime.now());

        if (expiredUrls.isEmpty()) {
            return;
        }

        urlRepository.deleteAll(expiredUrls);
    }
}