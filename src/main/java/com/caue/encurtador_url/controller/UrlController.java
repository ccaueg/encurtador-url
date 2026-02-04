package com.caue.encurtador_url.controller;

import com.caue.encurtador_url.model.Url;
import com.caue.encurtador_url.service.QrCodeService;
import com.caue.encurtador_url.service.UrlService;
import com.google.zxing.WriterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/url")
public class UrlController {
    private final UrlService urlService;
    private final QrCodeService qrCodeService;

    public UrlController(UrlService urlService, QrCodeService qrCodeService) {
        this.urlService = urlService;
        this.qrCodeService = qrCodeService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<Map<String, String>> shortenUrl(@RequestBody Map<String, String> request) {
        String originalUrl = request.get("url");

        if (originalUrl == null || originalUrl.isBlank()) {
            throw new IllegalArgumentException("A URL é obrigatória");
        }

        String shortUrl = "http://localhost:5050/url/" + urlService.shortenUrl(originalUrl);

        Map<String, String> response = new HashMap<>();
        response.put("url", shortUrl);

        try {
            String qrCodeBase64 = qrCodeService.generateQrCode(shortUrl);
            response.put("qrCode", qrCodeBase64);

            log.info("URL encurtada com sucesso e QR Code gerado: {}", shortUrl);
        } catch (IOException | WriterException exc) {
            log.error("Erro ao gerar QR Code para URL: {}", shortUrl, exc);

            response.put("qrCode", null);
            response.put("qrCodeError", "Erro ao gerar QR Code: " + exc.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<?> redirectUrl(@PathVariable String shortUrl) {
        Url url = urlService.getOriginalUrl(shortUrl);

        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(url.getOriginalUrl())).build();
    }
}
