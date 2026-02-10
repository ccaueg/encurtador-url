package com.caue.encurtador_url.service.qrcode;

import com.caue.encurtador_url.port.StoragePort;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class QrCodeService {
    private final StoragePort storagePort;

    @Value("${app.qrcode.width:300}")
    private int qrCodeWidth;

    @Value("${app.qrcode.height:300}")
    private int qrCodeHeight;

    public QrCodeService(StoragePort storagePort) {
        this.storagePort = storagePort;
    }

    public String generateQrCode(String url) throws IOException, WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, qrCodeWidth, qrCodeHeight);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            byte[] imageBytes = outputStream.toByteArray();

            return storagePort.uploadFile(imageBytes, UUID.randomUUID().toString(), "image/png");
        }
    }
}