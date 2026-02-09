package com.caue.encurtador_url.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ShortenUrlRequest {

    @NotBlank(message = "A URL é obrigatória")
    @Pattern(
            regexp = "^https?://.*",
            message = "URL deve começar com http:// ou https://"
    )
    private String url;
}