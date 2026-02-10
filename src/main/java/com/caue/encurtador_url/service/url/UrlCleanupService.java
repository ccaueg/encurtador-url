package com.caue.encurtador_url.service.url;

import com.caue.encurtador_url.model.Url;
import com.caue.encurtador_url.port.StoragePort;
import com.caue.encurtador_url.repository.UrlRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UrlCleanupService {

    private final UrlRepository urlRepository;
    private final StoragePort storage;

    public UrlCleanupService(UrlRepository urlRepository, StoragePort storage) {
        this.urlRepository = urlRepository;
        this.storage = storage;
    }

    @Transactional
    public void cleanupExpiredUrls() {
        List<Url> expiredUrls =
                urlRepository.findByExpirationDateBefore(LocalDateTime.now());

        for (Url url : expiredUrls) {
            storage.delete(url.getQrCodeKey());
            urlRepository.delete(url);
        }
    }
}
