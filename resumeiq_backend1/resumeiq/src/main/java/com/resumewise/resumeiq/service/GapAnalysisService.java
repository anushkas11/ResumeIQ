package com.resumewise.resumeiq.service;

import com.resumewise.resumeiq.dto.ai.GapReportDto;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class GapAnalysisService {

    private static final String SYSTEM_PROMPT = """
            You are an ATS (Applicant Tracking System) and resume analysis expert.
            You will be given a candidate's resume text and a target job description.

            Rules you MUST follow:
            1. Only compare what is written in the resume against the job description.
               Never invent or assume experience, skills, or tools that are not present
               in the resume text.
            2. Every score must be an integer between 0 and 100.
            3. "missingKeywords" = important skills/technologies/tools mentioned in the
               job description but NOT found anywhere in the resume.
            4. "weakBullets" = specific resume bullet points that are vague, lack metrics,
               or use weak verbs - quote them roughly as they appear.
            5. "suggestedAdditions" = skills/keywords from the job description that the
               candidate MIGHT plausibly have (e.g. closely related to something they
               already listed) but which are not explicitly written in the resume. These
               are only suggestions for the user to confirm - do not treat them as facts.
            6. Return ONLY the structured result - no extra commentary or markdown.
            """;

    private final ChatClient chatClient;

    public GapAnalysisService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public GapReportDto analyze(String resumeText, String jobDescriptionText) {

        String userPrompt = """
                RESUME TEXT:
                ---
                %s
                ---

                JOB DESCRIPTION:
                ---
                %s
                ---

                Analyze the gap between this resume and this job description.
                """.formatted(resumeText, jobDescriptionText);

        return chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .user(userPrompt)
                .call()
                .entity(GapReportDto.class);
    }
}
