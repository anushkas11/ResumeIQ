package com.resumewise.resumeiq.service;

import com.resumewise.resumeiq.dto.ai.OptimizedResumeDto;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResumeOptimizationService {

    private static final String SYSTEM_PROMPT = """
            You are an expert resume writer. You rewrite resumes to better match a
            target job description WITHOUT ever fabricating information.

            STRICT RULES (violating these is a critical failure):
            1. You may ONLY rephrase, reorder, or emphasize content that already
               exists in the original resume text.
            2. You may ONLY add a skill, tool, or keyword to the resume if it appears
               in the "confirmedAdditions" list provided to you. If confirmedAdditions
               is empty, add nothing new - only rephrase existing content.
            3. Never invent a company name, job title, project, metric, number, or
               achievement that is not present in the original resume text.
            4. Weave relevant job-description keywords naturally into existing bullet
               points where truthful and appropriate - do not just append a raw
               keyword list.
            5. Preserve all real dates, company names, and job titles exactly as given.
            6. Return ONLY the structured resume object - no extra commentary.
            """;

    private final ChatClient chatClient;

    public ResumeOptimizationService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public OptimizedResumeDto optimize(
            String originalResumeText,
            String jobDescriptionText,
            List<String> missingKeywords,
            List<String> weakBullets,
            List<String> confirmedAdditions
    ) {

        String userPrompt = """
                ORIGINAL RESUME TEXT:
                ---
                %s
                ---

                TARGET JOB DESCRIPTION:
                ---
                %s
                ---

                Keywords missing from the resume (weave in naturally where truthful,
                using only what's already in the resume plus confirmedAdditions below): %s

                Weak/vague bullet points to rewrite with stronger, more specific phrasing: %s

                Additions the user has explicitly CONFIRMED they possess (safe to add): %s

                Rewrite the resume into the structured format now, following all rules
                in the system prompt.
                """.formatted(
                originalResumeText,
                jobDescriptionText,
                missingKeywords,
                weakBullets,
                confirmedAdditions == null ? List.of() : confirmedAdditions
        );

        return chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .user(userPrompt)
                .call()
                .entity(OptimizedResumeDto.class);
    }
}
