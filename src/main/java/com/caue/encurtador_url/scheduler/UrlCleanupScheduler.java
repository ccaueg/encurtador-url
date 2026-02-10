package com.caue.encurtador_url.scheduler;

import com.caue.encurtador_url.service.url.UrlCleanupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UrlCleanupScheduler {

    private static final Logger logger = LoggerFactory.getLogger(UrlCleanupScheduler.class);
    private final UrlCleanupService cleanupService;

    public UrlCleanupScheduler(UrlCleanupService cleanupService) {
        this.cleanupService = cleanupService;
    }

    @Scheduled(cron = "0 0 3 * * *")
    public void runCleanup() {
        logger.info("Scheduler de limpeza de URLs iniciado.");
        try {
            cleanupService.cleanupExpiredUrls();
        } catch (Exception e) {
            logger.error("Erro ao executar limpeza de URLs", e);
        }
    }
}