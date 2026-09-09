package com.resumewise.resumeiq.dto.ai;

import java.util.List;

/**
 * Shape the AI is asked to return for gap analysis (Spring AI maps the model's
 * JSON response directly into this record via ChatClient's structured output).
 */
public record GapReportDto(
        int overallAtsScore,
        int keywordMatchScore,
        int formattingScore,
        int skillsCoverageScore,
        int experienceRelevanceScore,
        String summary,
        List<String> missingKeywords,
        List<String> weakBullets,
        // Skills/keywords the AI believes might be missing - NOT auto-applied.
        // The user must confirm each one before it can be used in the rewrite step.
        List<String> suggestedAdditions
) {
}
