package com.resumewise.resumeiq.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class AnalysisResponse {

    private Long id;
    private Long resumeId;
    private Long jobDescriptionId;
    private Integer overallAtsScore;
    private Integer keywordMatchScore;
    private Integer formattingScore;
    private Integer skillsCoverageScore;
    private Integer experienceRelevanceScore;
    private String summary;
    private List<String> missingKeywords;
    private List<String> weakBullets;
    private List<String> suggestedAdditions;
    private LocalDateTime analyzedAt;

    public AnalysisResponse(Long id, Long resumeId, Long jobDescriptionId, Integer overallAtsScore,
                             Integer keywordMatchScore, Integer formattingScore, Integer skillsCoverageScore,
                             Integer experienceRelevanceScore, String summary, List<String> missingKeywords,
                             List<String> weakBullets, List<String> suggestedAdditions, LocalDateTime analyzedAt) {
        this.id = id;
        this.resumeId = resumeId;
        this.jobDescriptionId = jobDescriptionId;
        this.overallAtsScore = overallAtsScore;
        this.keywordMatchScore = keywordMatchScore;
        this.formattingScore = formattingScore;
        this.skillsCoverageScore = skillsCoverageScore;
        this.experienceRelevanceScore = experienceRelevanceScore;
        this.summary = summary;
        this.missingKeywords = missingKeywords;
        this.weakBullets = weakBullets;
        this.suggestedAdditions = suggestedAdditions;
        this.analyzedAt = analyzedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getResumeId() {
        return resumeId;
    }

    public Long getJobDescriptionId() {
        return jobDescriptionId;
    }

    public Integer getOverallAtsScore() {
        return overallAtsScore;
    }

    public Integer getKeywordMatchScore() {
        return keywordMatchScore;
    }

    public Integer getFormattingScore() {
        return formattingScore;
    }

    public Integer getSkillsCoverageScore() {
        return skillsCoverageScore;
    }

    public Integer getExperienceRelevanceScore() {
        return experienceRelevanceScore;
    }

    public String getSummary() {
        return summary;
    }

    public List<String> getMissingKeywords() {
        return missingKeywords;
    }

    public List<String> getWeakBullets() {
        return weakBullets;
    }

    public List<String> getSuggestedAdditions() {
        return suggestedAdditions;
    }

    public LocalDateTime getAnalyzedAt() {
        return analyzedAt;
    }
}
