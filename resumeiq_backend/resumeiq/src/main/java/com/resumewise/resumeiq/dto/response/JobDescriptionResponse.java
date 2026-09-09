package com.resumewise.resumeiq.dto.response;

import java.time.LocalDateTime;

public class JobDescriptionResponse {

    private Long id;
    private Long userId;
    private String companyName;
    private String roleTitle;
    private String rawText;
    private LocalDateTime createdAt;

    public JobDescriptionResponse(Long id, Long userId, String companyName, String roleTitle,
                                   String rawText, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.companyName = companyName;
        this.roleTitle = roleTitle;
        this.rawText = rawText;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getRoleTitle() {
        return roleTitle;
    }

    public String getRawText() {
        return rawText;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
