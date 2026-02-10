package com.caue.encurtador_url.scheduler;

import com.caue.encurtador_url.service.url.UrlCleanupService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UrlCleanupScheduler {

    private final UrlCleanupService cleanupService;

    public UrlCleanupScheduler(UrlCleanupService cleanupService) {
        this.cleanupService = cleanupService;
    }

    @Scheduled(cron = "0 0 3 * * *")
    public void runCleanup() {
        cleanupService.cleanupExpiredUrls();
    }
}

