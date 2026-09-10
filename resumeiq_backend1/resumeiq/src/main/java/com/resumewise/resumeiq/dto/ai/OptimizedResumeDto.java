package com.resumewise.resumeiq.dto.ai;

import java.util.List;

/**
 * Shape the AI is asked to return for the resume rewrite. The backend renders
 * this structured object into the final PDF/DOCX - the AI never generates the
 * file itself, so formatting stays fully under our control.
 */
public record OptimizedResumeDto(
        String fullName,
        String contactInfo,
        String summary,
        List<String> skills,
        List<ExperienceEntry> experience,
        List<ProjectEntry> projects,
        List<String> education
) {
    public record ExperienceEntry(
            String title,
            String company,
            String duration,
            List<String> bullets
    ) {
    }

    public record ProjectEntry(
            String title,
            List<String> bullets
    ) {
    }
}
