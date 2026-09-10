package com.resumewise.resumeiq.service;

import com.resumewise.resumeiq.dto.request.AnalysisCreateRequest;
import com.resumewise.resumeiq.dto.response.AnalysisResponse;

import java.util.List;

public interface AnalysisService {

    AnalysisResponse createAnalysis(AnalysisCreateRequest request);

    AnalysisResponse getAnalysisById(Long id);

    List<AnalysisResponse> getAnalysesByResumeId(Long resumeId);
}
