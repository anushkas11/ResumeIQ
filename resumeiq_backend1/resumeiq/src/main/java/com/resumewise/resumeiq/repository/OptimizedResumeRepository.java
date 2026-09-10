package com.resumewise.resumeiq.repository;

import com.resumewise.resumeiq.entity.OptimizedResume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptimizedResumeRepository extends JpaRepository<OptimizedResume, Long> {

    List<OptimizedResume> findByAnalysisIdOrderByVersionAsc(Long analysisId);
}
