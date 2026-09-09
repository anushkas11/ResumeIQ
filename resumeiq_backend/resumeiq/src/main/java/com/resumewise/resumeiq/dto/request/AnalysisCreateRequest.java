package com.resumewise.resumeiq.dto.request;

import jakarta.validation.constraints.NotNull;

/**
 * The AI does the actual scoring - this request just tells the backend
 * which resume to compare against which job description.
 */
public class AnalysisCreateRequest {

    @NotNull(message = "resumeId is required")
    private Long resumeId;

    @NotNull(message = "jobDescriptionId is required")
    private Long jobDescriptionId;

    public Long getResumeId() {
        return resumeId;
    }

    public void setResumeId(Long resumeId) {
        this.resumeId = resumeId;
    }

    public Long getJobDescriptionId() {
        return jobDescriptionId;
    }

    public void setJobDescriptionId(Long jobDescriptionId) {
        this.jobDescriptionId = jobDescriptionId;
    }
}
