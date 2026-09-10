package com.resumewise.resumeiq.repository;

import com.resumewise.resumeiq.entity.Analysis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnalysisRepository extends JpaRepository<Analysis, Long> {

    List<Analysis> findByResumeId(Long resumeId);
}