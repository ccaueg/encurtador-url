package com.caue.encurtador_url.controller;

import com.caue.encurtador_url.dto.ShortenUrlRequest;
import com.caue.encurtador_url.dto.ShortenUrlResponse;
import com.caue.encurtador_url.model.Url;
import com.caue.encurtador_url.service.QrCodeService;
import com.caue.encurtador_url.service.UrlService;
import com.google.zxing.WriterException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/url")
public class UrlController {

    private final UrlService urlService;
    private final QrCodeService qrCodeService;

    @Value("${app.base-url:http://localhost:5050}")
    private String baseUrl;

    public UrlController(UrlService urlService, QrCodeService qrCodeService) {
        this.urlService = urlService;
        this.qrCodeService = qrCodeService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<ShortenUrlResponse> shortenUrl(@Valid @RequestBody ShortenUrlRequest request) {
        String shortCode = urlService.shortenUrl(request.getUrl());
        String shortUrl = baseUrl + "/url/" + shortCode;

        ShortenUrlResponse.ShortenUrlResponseBuilder responseBuilder = ShortenUrlResponse.builder().url(shortUrl);

        try {
            String qrCode = qrCodeService.generateQrCode(shortUrl);
            responseBuilder.qrCode(qrCode);
        } catch (IOException | WriterException e) {
            responseBuilder.qrCodeError("Não foi possível gerar o QR Code");
        }

        return ResponseEntity.ok(responseBuilder.build());
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Void> redirectUrl(@PathVariable String shortUrl) {
        Url url = urlService.getOriginalUrl(shortUrl);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(url.getOriginalUrl())).build();
    }
}