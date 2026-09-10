package com.resumewise.resumeiq.dto.response;

import java.time.LocalDateTime;

public class ResumeResponse {

    private Long id;
    private Long userId;
    private String fileName;
    private String storageReference;
    private LocalDateTime uploadedAt;

    public ResumeResponse() {
    }

    public ResumeResponse(
            Long id,
            Long userId,
            String fileName,
            String storageReference,
            LocalDateTime uploadedAt
    ) {
        this.id = id;
        this.userId = userId;
        this.fileName = fileName;
        this.storageReference = storageReference;
        this.uploadedAt = uploadedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getFileName() {
        return fileName;
    }

    public String getStorageReference() {
        return storageReference;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }
}