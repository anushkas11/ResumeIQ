package com.resumewise.resumeiq.service.impl;

import com.resumewise.resumeiq.dto.ai.GapReportDto;
import com.resumewise.resumeiq.dto.request.AnalysisCreateRequest;
import com.resumewise.resumeiq.dto.response.AnalysisResponse;
import com.resumewise.resumeiq.entity.Analysis;
import com.resumewise.resumeiq.entity.JobDescription;
import com.resumewise.resumeiq.entity.Resume;
import com.resumewise.resumeiq.exception.ResourceNotFoundException;
import com.resumewise.resumeiq.repository.AnalysisRepository;
import com.resumewise.resumeiq.repository.JobDescriptionRepository;
import com.resumewise.resumeiq.repository.ResumeRepository;
import com.resumewise.resumeiq.security.CurrentUserProvider;
import com.resumewise.resumeiq.service.AnalysisService;
import com.resumewise.resumeiq.service.GapAnalysisService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalysisServiceImpl implements AnalysisService {

    private final AnalysisRepository analysisRepository;
    private final ResumeRepository resumeRepository;
    private final JobDescriptionRepository jobDescriptionRepository;
    private final GapAnalysisService gapAnalysisService;
    private final CurrentUserProvider currentUserProvider;

    public AnalysisServiceImpl(
            AnalysisRepository analysisRepository,
            ResumeRepository resumeRepository,
            JobDescriptionRepository jobDescriptionRepository,
            GapAnalysisService gapAnalysisService,
            CurrentUserProvider currentUserProvider
    ) {
        this.analysisRepository = analysisRepository;
        this.resumeRepository = resumeRepository;
        this.jobDescriptionRepository = jobDescriptionRepository;
        this.gapAnalysisService = gapAnalysisService;
        this.currentUserProvider = currentUserProvider;
    }

    @Override
    public AnalysisResponse createAnalysis(AnalysisCreateRequest request) {

        Long currentUserId = currentUserProvider.getCurrentUserId();

        Resume resume = resumeRepository.findById(request.getResumeId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Resume not found with id: " + request.getResumeId())
                );

        if (!resume.getUser().getId().equals(currentUserId)) {
            throw new ResourceNotFoundException("Resume not found with id: " + request.getResumeId());
        }

        JobDescription jobDescription = jobDescriptionRepository.findById(request.getJobDescriptionId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Job description not found with id: " + request.getJobDescriptionId())
                );

        if (!jobDescription.getUser().getId().equals(currentUserId)) {
            throw new ResourceNotFoundException(
                    "Job description not found with id: " + request.getJobDescriptionId());
        }

        // --- The actual AI call ---
        GapReportDto result = gapAnalysisService.analyze(
                resume.getExtractedText(),
                jobDescription.getRawText()
        );

        Analysis analysis = new Analysis(resume, jobDescription);
        analysis.setOverallAtsScore(result.overallAtsScore());
        analysis.setKeywordMatchScore(result.keywordMatchScore());
        analysis.setFormattingScore(result.formattingScore());
        analysis.setSkillsCoverageScore(result.skillsCoverageScore());
        analysis.setExperienceRelevanceScore(result.experienceRelevanceScore());
        analysis.setSummary(result.summary());
        analysis.setMissingKeywords(result.missingKeywords());
        analysis.setWeakBullets(result.weakBullets());
        analysis.setSuggestedAdditions(result.suggestedAdditions());

        Analysis savedAnalysis = analysisRepository.save(analysis);

        return mapToResponse(savedAnalysis);
    }

    @Override
    public AnalysisResponse getAnalysisById(Long id) {

        Analysis analysis = analysisRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Analysis not found with id: " + id)
                );

        assertOwnership(analysis);

        return mapToResponse(analysis);
    }

    @Override
    public List<AnalysisResponse> getAnalysesByResumeId(Long resumeId) {

        Long currentUserId = currentUserProvider.getCurrentUserId();

        return analysisRepository.findByResumeId(resumeId)
                .stream()
                .filter(a -> a.getResume().getUser().getId().equals(currentUserId))
                .map(this::mapToResponse)
                .toList();
    }

    private void assertOwnership(Analysis analysis) {
        Long currentUserId = currentUserProvider.getCurrentUserId();
        if (!analysis.getResume().getUser().getId().equals(currentUserId)) {
            throw new ResourceNotFoundException("Analysis not found with id: " + analysis.getId());
        }
    }

    private AnalysisResponse mapToResponse(Analysis analysis) {

        return new AnalysisResponse(
                analysis.getId(),
                analysis.getResume().getId(),
                analysis.getJobDescription().getId(),
                analysis.getOverallAtsScore(),
                analysis.getKeywordMatchScore(),
                analysis.getFormattingScore(),
                analysis.getSkillsCoverageScore(),
                analysis.getExperienceRelevanceScore(),
                analysis.getSummary(),
                analysis.getMissingKeywords(),
                analysis.getWeakBullets(),
                analysis.getSuggestedAdditions(),
                analysis.getAnalyzedAt()
        );
    }
}
