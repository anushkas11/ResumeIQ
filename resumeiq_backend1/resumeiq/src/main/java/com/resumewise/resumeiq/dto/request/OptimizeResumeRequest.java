package com.resumewise.resumeiq.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class OptimizeResumeRequest {

    @NotNull(message = "analysisId is required")
    private Long analysisId;

    // Subset of the analysis's suggestedAdditions that the user has confirmed
    // they genuinely possess. Anything NOT in this list must never be added
    // to the rewritten resume - this is the anti-hallucination guardrail.
    private List<String> confirmedAdditions;

    public Long getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(Long analysisId) {
        this.analysisId = analysisId;
    }

    public List<String> getConfirmedAdditions() {
        return confirmedAdditions;
    }

    public void setConfirmedAdditions(List<String> confirmedAdditions) {
        this.confirmedAdditions = confirmedAdditions;
    }
}
