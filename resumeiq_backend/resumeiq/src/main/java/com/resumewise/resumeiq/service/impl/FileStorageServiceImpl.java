package com.resumewise.resumeiq.service.impl;

import com.resumewise.resumeiq.service.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private static final String UPLOAD_DIRECTORY = "uploads";

    @Override
    public String store(MultipartFile file) throws IOException {

        Path uploadPath = Paths.get(UPLOAD_DIRECTORY)
                .toAbsolutePath()
                .normalize();

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFileName =
                StringUtils.cleanPath(
                        file.getOriginalFilename()
                );

        String extension = "";

        int lastDot =
                originalFileName.lastIndexOf(".");

        if (lastDot >= 0) {
            extension =
                    originalFileName.substring(lastDot)
                            .toLowerCase();
        }

        String storedFileName =
                UUID.randomUUID() + extension;

        Path targetPath =
                uploadPath.resolve(storedFileName)
                        .normalize();

        Files.copy(
                file.getInputStream(),
                targetPath,
                StandardCopyOption.REPLACE_EXISTING
        );

        return targetPath.toString();
    }
}