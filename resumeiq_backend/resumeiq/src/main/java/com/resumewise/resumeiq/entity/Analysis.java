package com.resumewise.resumeiq.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the AI-generated gap analysis for a (Resume, JobDescription) pair.
 * This is the "GapReport" described in the project specification.
 */
@Entity
@Table(name = "analyses")
public class Analysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_description_id", nullable = false)
    private JobDescription jobDescription;

    private Integer overallAtsScore;
    private Integer keywordMatchScore;
    private Integer formattingScore;
    private Integer skillsCoverageScore;
    private Integer experienceRelevanceScore;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @ElementCollection
    @CollectionTable(name = "analysis_missing_keywords", joinColumns = @JoinColumn(name = "analysis_id"))
    @Column(name = "keyword", columnDefinition = "TEXT")
    private List<String> missingKeywords = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "analysis_weak_bullets", joinColumns = @JoinColumn(name = "analysis_id"))
    @Column(name = "bullet", columnDefinition = "TEXT")
    private List<String> weakBullets = new ArrayList<>();

    // Skills/keywords the AI thinks might be missing - the user MUST confirm these
    // before they are ever added to the optimized resume (anti-hallucination guardrail).
    @ElementCollection
    @CollectionTable(name = "analysis_suggested_additions", joinColumns = @JoinColumn(name = "analysis_id"))
    @Column(name = "suggestion", columnDefinition = "TEXT")
    private List<String> suggestedAdditions = new ArrayList<>();

    private LocalDateTime analyzedAt;

    public Analysis() {
    }

    public Analysis(Resume resume, JobDescription jobDescription) {
        this.resume = resume;
        this.jobDescription = jobDescription;
        this.analyzedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Resume getResume() {
        return resume;
    }

    public void setResume(Resume resume) {
        this.resume = resume;
    }

    public JobDescription getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(JobDescription jobDescription) {
        this.jobDescription = jobDescription;
    }

    public Integer getOverallAtsScore() {
        return overallAtsScore;
    }

    public void setOverallAtsScore(Integer overallAtsScore) {
        this.overallAtsScore = overallAtsScore;
    }

    public Integer getKeywordMatchScore() {
        return keywordMatchScore;
    }

    public void setKeywordMatchScore(Integer keywordMatchScore) {
        this.keywordMatchScore = keywordMatchScore;
    }

    public Integer getFormattingScore() {
        return formattingScore;
    }

    public void setFormattingScore(Integer formattingScore) {
        this.formattingScore = formattingScore;
    }

    public Integer getSkillsCoverageScore() {
        return skillsCoverageScore;
    }

    public void setSkillsCoverageScore(Integer skillsCoverageScore) {
        this.skillsCoverageScore = skillsCoverageScore;
    }

    public Integer getExperienceRelevanceScore() {
        return experienceRelevanceScore;
    }

    public void setExperienceRelevanceScore(Integer experienceRelevanceScore) {
        this.experienceRelevanceScore = experienceRelevanceScore;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<String> getMissingKeywords() {
        return missingKeywords;
    }

    public void setMissingKeywords(List<String> missingKeywords) {
        this.missingKeywords = missingKeywords;
    }

    public List<String> getWeakBullets() {
        return weakBullets;
    }

    public void setWeakBullets(List<String> weakBullets) {
        this.weakBullets = weakBullets;
    }

    public List<String> getSuggestedAdditions() {
        return suggestedAdditions;
    }

    public void setSuggestedAdditions(List<String> suggestedAdditions) {
        this.suggestedAdditions = suggestedAdditions;
    }

    public LocalDateTime getAnalyzedAt() {
        return analyzedAt;
    }

    public void setAnalyzedAt(LocalDateTime analyzedAt) {
        this.analyzedAt = analyzedAt;
    }
}
