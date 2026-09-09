package com.resumewise.resumeiq.controller;

import com.resumewise.resumeiq.dto.request.JobDescriptionCreateRequest;
import com.resumewise.resumeiq.dto.response.JobDescriptionResponse;
import com.resumewise.resumeiq.service.JobDescriptionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-descriptions")
public class JobDescriptionController {

    private final JobDescriptionService jobDescriptionService;

    public JobDescriptionController(JobDescriptionService jobDescriptionService) {
        this.jobDescriptionService = jobDescriptionService;
    }

    @PostMapping
    public ResponseEntity<JobDescriptionResponse> createJobDescription(
            @Valid @RequestBody JobDescriptionCreateRequest request
    ) {
        JobDescriptionResponse response = jobDescriptionService.createJobDescription(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobDescriptionResponse> getJobDescriptionById(@PathVariable Long id) {
        return ResponseEntity.ok(jobDescriptionService.getJobDescriptionById(id));
    }

    @GetMapping
    public ResponseEntity<List<JobDescriptionResponse>> getMyJobDescriptions() {
        return ResponseEntity.ok(jobDescriptionService.getMyJobDescriptions());
    }
}
