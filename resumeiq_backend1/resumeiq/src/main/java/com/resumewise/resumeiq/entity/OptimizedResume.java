package com.resumewise.resumeiq.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "optimized_resumes")
public class OptimizedResume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analysis_id", nullable = false)
    private Analysis analysis;

    // Full structured resume content, stored as a JSON string.
    // Shape: { "summary": "...", "skills": [...], "experience": [...], "projects": [...], "education": [...] }
    @Lob
    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String sectionsJson;

    private String templateUsed;

    private Integer version;

    private LocalDateTime createdAt;

    public OptimizedResume() {
    }

    public OptimizedResume(Analysis analysis, String sectionsJson, String templateUsed, Integer version) {
        this.analysis = analysis;
        this.sectionsJson = sectionsJson;
        this.templateUsed = templateUsed;
        this.version = version;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Analysis getAnalysis() {
        return analysis;
    }

    public void setAnalysis(Analysis analysis) {
        this.analysis = analysis;
    }

    public String getSectionsJson() {
        return sectionsJson;
    }

    public void setSectionsJson(String sectionsJson) {
        this.sectionsJson = sectionsJson;
    }

    public String getTemplateUsed() {
        return templateUsed;
    }

    public void setTemplateUsed(String templateUsed) {
        this.templateUsed = templateUsed;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
