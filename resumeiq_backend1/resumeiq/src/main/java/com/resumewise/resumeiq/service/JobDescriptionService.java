package com.resumewise.resumeiq.service;

import com.resumewise.resumeiq.dto.request.JobDescriptionCreateRequest;
import com.resumewise.resumeiq.dto.response.JobDescriptionResponse;

import java.util.List;

public interface JobDescriptionService {

    JobDescriptionResponse createJobDescription(JobDescriptionCreateRequest request);

    JobDescriptionResponse getJobDescriptionById(Long id);

    List<JobDescriptionResponse> getMyJobDescriptions();
}
