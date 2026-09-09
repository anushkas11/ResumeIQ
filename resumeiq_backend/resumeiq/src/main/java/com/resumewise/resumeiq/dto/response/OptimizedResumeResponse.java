package com.resumewise.resumeiq.dto.response;

import com.resumewise.resumeiq.dto.ai.OptimizedResumeDto;

import java.time.LocalDateTime;

public class OptimizedResumeResponse {

    private Long id;
    private Long analysisId;
    private OptimizedResumeDto sections;
    private String templateUsed;
    private Integer version;
    private LocalDateTime createdAt;

    public OptimizedResumeResponse(Long id, Long analysisId, OptimizedResumeDto sections,
                                    String templateUsed, Integer version, LocalDateTime createdAt) {
        this.id = id;
        this.analysisId = analysisId;
        this.sections = sections;
        this.templateUsed = templateUsed;
        this.version = version;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getAnalysisId() {
        return analysisId;
    }

    public OptimizedResumeDto getSections() {
        return sections;
    }

    public String getTemplateUsed() {
        return templateUsed;
    }

    public Integer getVersion() {
        return version;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
