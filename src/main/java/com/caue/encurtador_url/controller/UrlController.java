package com.caue.encurtador_url.controller;

import com.caue.encurtador_url.exception.ResourceNotFoundException;
import com.caue.encurtador_url.model.Url;
import com.caue.encurtador_url.service.UrlService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/url")
public class UrlController {
    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<Map<String, String>> shortenUrl(@RequestBody Map<String, String> request) {
        String originalUrl = request.get("url");

        if (originalUrl == null || originalUrl.isBlank()) {
            throw new IllegalArgumentException("A URL é obrigatória");
        }

        String shortUrl = urlService.shortenUrl(originalUrl);

        Map<String, String> response = new HashMap<>();
        response.put("url", "http://localhost:3000/url/" + shortUrl);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<?> redirectUrl(@PathVariable String shortUrl) {
        Url url = urlService.getOriginalUrl(shortUrl);

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(url.getOriginalUrl()))
                .build();
    }
}
