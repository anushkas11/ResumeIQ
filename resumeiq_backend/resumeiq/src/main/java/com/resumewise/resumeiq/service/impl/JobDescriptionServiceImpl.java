package com.resumewise.resumeiq.service.impl;

import com.resumewise.resumeiq.dto.request.JobDescriptionCreateRequest;
import com.resumewise.resumeiq.dto.response.JobDescriptionResponse;
import com.resumewise.resumeiq.entity.JobDescription;
import com.resumewise.resumeiq.entity.User;
import com.resumewise.resumeiq.exception.ResourceNotFoundException;
import com.resumewise.resumeiq.repository.JobDescriptionRepository;
import com.resumewise.resumeiq.repository.UserRepository;
import com.resumewise.resumeiq.security.CurrentUserProvider;
import com.resumewise.resumeiq.service.JobDescriptionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobDescriptionServiceImpl implements JobDescriptionService {

    private final JobDescriptionRepository jobDescriptionRepository;
    private final UserRepository userRepository;
    private final CurrentUserProvider currentUserProvider;

    public JobDescriptionServiceImpl(
            JobDescriptionRepository jobDescriptionRepository,
            UserRepository userRepository,
            CurrentUserProvider currentUserProvider
    ) {
        this.jobDescriptionRepository = jobDescriptionRepository;
        this.userRepository = userRepository;
        this.currentUserProvider = currentUserProvider;
    }

    @Override
    public JobDescriptionResponse createJobDescription(JobDescriptionCreateRequest request) {

        Long currentUserId = currentUserProvider.getCurrentUserId();

        User user = userRepository.findById(currentUserId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + currentUserId)
                );

        JobDescription jd = new JobDescription(
                user,
                request.getCompanyName(),
                request.getRoleTitle(),
                request.getRawText()
        );

        return mapToResponse(jobDescriptionRepository.save(jd));
    }

    @Override
    public JobDescriptionResponse getJobDescriptionById(Long id) {

        JobDescription jd = jobDescriptionRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Job description not found with id: " + id)
                );

        assertOwnership(jd);

        return mapToResponse(jd);
    }

    @Override
    public List<JobDescriptionResponse> getMyJobDescriptions() {

        Long currentUserId = currentUserProvider.getCurrentUserId();

        return jobDescriptionRepository.findByUserId(currentUserId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private void assertOwnership(JobDescription jd) {
        Long currentUserId = currentUserProvider.getCurrentUserId();
        if (!jd.getUser().getId().equals(currentUserId)) {
            throw new ResourceNotFoundException("Job description not found with id: " + jd.getId());
        }
    }

    private JobDescriptionResponse mapToResponse(JobDescription jd) {
        return new JobDescriptionResponse(
                jd.getId(),
                jd.getUser().getId(),
                jd.getCompanyName(),
                jd.getRoleTitle(),
                jd.getRawText(),
                jd.getCreatedAt()
        );
    }
}
