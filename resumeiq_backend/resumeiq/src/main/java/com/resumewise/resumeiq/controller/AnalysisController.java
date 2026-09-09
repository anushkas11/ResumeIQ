package com.resumewise.resumeiq.controller;

import com.resumewise.resumeiq.dto.request.AnalysisCreateRequest;
import com.resumewise.resumeiq.dto.response.AnalysisResponse;
import com.resumewise.resumeiq.service.AnalysisService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analyses")
public class AnalysisController {

    private final AnalysisService analysisService;

    public AnalysisController(
            AnalysisService analysisService
    ) {
        this.analysisService = analysisService;
    }

    @PostMapping
    public ResponseEntity<AnalysisResponse> createAnalysis(
            @Valid @RequestBody AnalysisCreateRequest request
    ) {

        AnalysisResponse response =
                analysisService.createAnalysis(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnalysisResponse> getAnalysisById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                analysisService.getAnalysisById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<AnalysisResponse>>
    getAnalysesByResumeId(
            @RequestParam Long resumeId
    ) {

        return ResponseEntity.ok(
                analysisService
                        .getAnalysesByResumeId(resumeId)
        );
    }
}