package com.resumewise.resumeiq.controller;

import com.resumewise.resumeiq.dto.ai.OptimizedResumeDto;
import com.resumewise.resumeiq.dto.request.OptimizeResumeRequest;
import com.resumewise.resumeiq.dto.response.OptimizedResumeResponse;
import com.resumewise.resumeiq.service.OptimizedResumeService;
import com.resumewise.resumeiq.service.ResumeExportService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/optimize")
public class OptimizationController {

    private final OptimizedResumeService optimizedResumeService;
    private final ResumeExportService resumeExportService;

    public OptimizationController(
            OptimizedResumeService optimizedResumeService,
            ResumeExportService resumeExportService
    ) {
        this.optimizedResumeService = optimizedResumeService;
        this.resumeExportService = resumeExportService;
    }

    @PostMapping
    public ResponseEntity<OptimizedResumeResponse> optimize(
            @Valid @RequestBody OptimizeResumeRequest request
    ) {
        OptimizedResumeResponse response = optimizedResumeService.optimize(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OptimizedResumeResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(optimizedResumeService.getById(id));
    }

    /**
     * Downloads the optimized resume as a file.
     * format=docx -> .docx, format=pdf (default) -> .pdf
     */
    @GetMapping("/{id}/export")
    public ResponseEntity<byte[]> export(
            @PathVariable Long id,
            @RequestParam(defaultValue = "pdf") String format
    ) {
        OptimizedResumeDto sections = optimizedResumeService.getSectionsForExport(id);

        byte[] fileBytes;
        MediaType mediaType;
        String filename;

        if ("docx".equalsIgnoreCase(format)) {
            fileBytes = resumeExportService.toDocx(sections);
            mediaType = MediaType.parseMediaType(
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            filename = "optimized-resume-" + id + ".docx";
        } else {
            fileBytes = resumeExportService.toPdf(sections);
            mediaType = MediaType.APPLICATION_PDF;
            filename = "optimized-resume-" + id + ".pdf";
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(fileBytes);
    }
}
