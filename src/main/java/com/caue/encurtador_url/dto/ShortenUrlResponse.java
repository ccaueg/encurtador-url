package com.caue.encurtador_url.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShortenUrlResponse {
    private String url;
    private String qrCode;
    private String qrCodeError;
}