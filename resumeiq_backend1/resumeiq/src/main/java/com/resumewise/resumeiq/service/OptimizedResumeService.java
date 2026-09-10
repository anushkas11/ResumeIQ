package com.resumewise.resumeiq.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resumewise.resumeiq.dto.ai.OptimizedResumeDto;
import com.resumewise.resumeiq.dto.request.OptimizeResumeRequest;
import com.resumewise.resumeiq.dto.response.OptimizedResumeResponse;
import com.resumewise.resumeiq.entity.Analysis;
import com.resumewise.resumeiq.entity.OptimizedResume;
import com.resumewise.resumeiq.exception.ResourceNotFoundException;
import com.resumewise.resumeiq.repository.AnalysisRepository;
import com.resumewise.resumeiq.repository.OptimizedResumeRepository;
import com.resumewise.resumeiq.security.CurrentUserProvider;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class OptimizedResumeService {

    private final AnalysisRepository analysisRepository;
    private final OptimizedResumeRepository optimizedResumeRepository;
    private final ResumeOptimizationService resumeOptimizationService;
    private final CurrentUserProvider currentUserProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OptimizedResumeService(
            AnalysisRepository analysisRepository,
            OptimizedResumeRepository optimizedResumeRepository,
            ResumeOptimizationService resumeOptimizationService,
            CurrentUserProvider currentUserProvider
    ) {
        this.analysisRepository = analysisRepository;
        this.optimizedResumeRepository = optimizedResumeRepository;
        this.resumeOptimizationService = resumeOptimizationService;
        this.currentUserProvider = currentUserProvider;
    }

    public OptimizedResumeResponse optimize(OptimizeResumeRequest request) {

        Long currentUserId = currentUserProvider.getCurrentUserId();

        Analysis analysis = analysisRepository.findById(request.getAnalysisId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Analysis not found with id: " + request.getAnalysisId())
                );

        if (!analysis.getResume().getUser().getId().equals(currentUserId)) {
            throw new ResourceNotFoundException("Analysis not found with id: " + request.getAnalysisId());
        }

        // Guardrail: only additions that were BOTH suggested by the AI AND
        // explicitly confirmed by the user are allowed through. Anything the
        // user might have injected that wasn't actually suggested is dropped.
        Set<String> allowedSuggestions = new HashSet<>(analysis.getSuggestedAdditions());
        List<String> confirmedAdditions = (request.getConfirmedAdditions() == null)
                ? List.of()
                : request.getConfirmedAdditions().stream()
                        .filter(allowedSuggestions::contains)
                        .toList();

        OptimizedResumeDto result = resumeOptimizationService.optimize(
                analysis.getResume().getExtractedText(),
                analysis.getJobDescription().getRawText(),
                analysis.getMissingKeywords(),
                analysis.getWeakBullets(),
                confirmedAdditions
        );

        String sectionsJson;
        try {
            sectionsJson = objectMapper.writeValueAsString(result);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize optimized resume: " + e.getMessage(), e);
        }

        int nextVersion = optimizedResumeRepository
                .findByAnalysisIdOrderByVersionAsc(analysis.getId())
                .size() + 1;

        OptimizedResume optimizedResume = new OptimizedResume(
                analysis, sectionsJson, "ats-safe-single-column", nextVersion
        );

        OptimizedResume saved = optimizedResumeRepository.save(optimizedResume);

        return mapToResponse(saved, result);
    }

    public OptimizedResumeResponse getById(Long id) {

        OptimizedResume optimizedResume = optimizedResumeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Optimized resume not found with id: " + id)
                );

        assertOwnership(optimizedResume);

        return mapToResponse(optimizedResume, deserialize(optimizedResume.getSectionsJson()));
    }

    /**
     * Used by the export controller - returns the parsed DTO ready to hand to
     * ResumeExportService, after verifying ownership.
     */
    public OptimizedResumeDto getSectionsForExport(Long id) {
        OptimizedResume optimizedResume = optimizedResumeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Optimized resume not found with id: " + id)
                );
        assertOwnership(optimizedResume);
        return deserialize(optimizedResume.getSectionsJson());
    }

    private void assertOwnership(OptimizedResume optimizedResume) {
        Long currentUserId = currentUserProvider.getCurrentUserId();
        Long ownerId = optimizedResume.getAnalysis().getResume().getUser().getId();
        if (!ownerId.equals(currentUserId)) {
            throw new ResourceNotFoundException("Optimized resume not found with id: " + optimizedResume.getId());
        }
    }

    private OptimizedResumeDto deserialize(String json) {
        try {
            return objectMapper.readValue(json, OptimizedResumeDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read stored optimized resume: " + e.getMessage(), e);
        }
    }

    private OptimizedResumeResponse mapToResponse(OptimizedResume entity, OptimizedResumeDto sections) {
        return new OptimizedResumeResponse(
                entity.getId(),
                entity.getAnalysis().getId(),
                sections,
                entity.getTemplateUsed(),
                entity.getVersion(),
                entity.getCreatedAt()
        );
    }
}
