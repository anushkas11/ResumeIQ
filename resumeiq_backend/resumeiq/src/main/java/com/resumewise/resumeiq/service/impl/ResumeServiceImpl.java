package com.resumewise.resumeiq.service.impl;

import com.resumewise.resumeiq.dto.response.ResumeResponse;
import com.resumewise.resumeiq.entity.Resume;
import com.resumewise.resumeiq.entity.User;
import com.resumewise.resumeiq.exception.FileStorageException;
import com.resumewise.resumeiq.exception.ResourceNotFoundException;
import com.resumewise.resumeiq.repository.ResumeRepository;
import com.resumewise.resumeiq.repository.UserRepository;
import com.resumewise.resumeiq.security.CurrentUserProvider;
import com.resumewise.resumeiq.service.FileParsingService;
import com.resumewise.resumeiq.service.FileStorageService;
import com.resumewise.resumeiq.service.ResumeService;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ResumeServiceImpl
        implements ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final FileParsingService fileParsingService;
    private final FileStorageService fileStorageService;
    private final CurrentUserProvider currentUserProvider;

    public ResumeServiceImpl(
            ResumeRepository resumeRepository,
            UserRepository userRepository,
            FileParsingService fileParsingService,
            FileStorageService fileStorageService,
            CurrentUserProvider currentUserProvider
    ) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.fileParsingService = fileParsingService;
        this.fileStorageService = fileStorageService;
        this.currentUserProvider = currentUserProvider;
    }

    @Override
    public ResumeResponse uploadResume(
            MultipartFile file
    ) {

        Long currentUserId =
                currentUserProvider.getCurrentUserId();

        User user =
                userRepository.findById(currentUserId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Authenticated user not found."
                                )
                        );

        // Validate and extract before saving anything.
        String extractedText =
                fileParsingService.extractText(file);

        String storageReference;

        try {

            storageReference =
                    fileStorageService.store(file);

        } catch (IOException e) {

            throw new FileStorageException(
                    "Failed to store the uploaded resume.",
                    e
            );
        }

        Resume resume =
                new Resume(
                        user,
                        file.getOriginalFilename(),
                        storageReference,
                        extractedText
                );

        Resume savedResume =
                resumeRepository.save(resume);

        return mapToResponse(savedResume);
    }

    @Override
    public ResumeResponse getResumeById(
            Long id
    ) {

        Resume resume =
                resumeRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Resume not found with id: " + id
                                )
                        );

        assertOwnership(resume);

        return mapToResponse(resume);
    }

    @Override
    public List<ResumeResponse> getMyResumes() {

        Long currentUserId =
                currentUserProvider.getCurrentUserId();

        return resumeRepository
                .findByUserId(currentUserId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private void assertOwnership(
            Resume resume
    ) {

        Long currentUserId =
                currentUserProvider.getCurrentUserId();

        if (!resume.getUser().getId()
                .equals(currentUserId)) {

            throw new ResourceNotFoundException(
                    "Resume not found with id: "
                            + resume.getId()
            );
        }
    }

    private ResumeResponse mapToResponse(
            Resume resume
    ) {

        return new ResumeResponse(
                resume.getId(),
                resume.getUser().getId(),
                resume.getFileName(),
                resume.getStorageReference(),
                resume.getUploadedAt()
        );
    }
}