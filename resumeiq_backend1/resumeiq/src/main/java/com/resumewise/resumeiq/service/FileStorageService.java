package com.resumewise.resumeiq.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {

    String store(MultipartFile file) throws IOException;
}