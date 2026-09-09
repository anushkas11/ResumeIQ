package com.resumewise.resumeiq.controller;

import com.resumewise.resumeiq.dto.response.ResumeResponse;
import com.resumewise.resumeiq.service.ResumeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    /**
     * Upload a resume (PDF or DOCX). Text is extracted server-side via Tika
     * and stored against the currently authenticated user.
     */
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<ResumeResponse> uploadResume(
            @RequestParam("file") MultipartFile file
    ) {
        ResumeResponse response = resumeService.uploadResume(file);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResumeResponse> getResumeById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(resumeService.getResumeById(id));
    }

    /**
     * Lists resumes belonging to the currently authenticated user.
     * (No userId query param anymore - it's derived from the JWT, not the client.)
     */
    @GetMapping
    public ResponseEntity<List<ResumeResponse>> getMyResumes() {
        return ResponseEntity.ok(resumeService.getMyResumes());
    }
}
