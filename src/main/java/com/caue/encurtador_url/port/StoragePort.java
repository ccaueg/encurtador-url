package com.caue.encurtador_url.port;

public interface StoragePort {
    String uploadFile(byte[] fileData, String fileName, String contentType);
}
