package com.resumewise.resumeiq.service;

import com.resumewise.resumeiq.dto.response.ResumeResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ResumeService {

    ResumeResponse uploadResume(MultipartFile file);

    ResumeResponse getResumeById(Long id);

    List<ResumeResponse> getMyResumes();
}
