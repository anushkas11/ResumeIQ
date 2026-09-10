package com.resumewise.resumeiq.service;

import com.resumewise.resumeiq.exception.InvalidFileException;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileParsingService {

    private static final long MAX_FILE_SIZE =
            5 * 1024 * 1024;

    private final Tika tika = new Tika();

    public String extractText(MultipartFile file) {

        validateFile(file);

        try {

            String detectedType =
                    tika.detect(
                            file.getInputStream(),
                            file.getOriginalFilename()
                    );

            validateContentType(detectedType);

            String text =
                    tika.parseToString(
                            file.getInputStream()
                    );

            if (text == null || text.isBlank()) {

                throw new InvalidFileException(
                        "Could not extract text from the uploaded resume. " +
                                "Please upload a text-based PDF or DOCX file."
                );
            }

            String cleanedText =
                    cleanExtractedText(text);

            if (cleanedText.isBlank()) {

                throw new InvalidFileException(
                        "The uploaded resume does not contain readable text."
                );
            }

            return cleanedText;

        } catch (IOException e) {

            throw new InvalidFileException(
                    "Unable to read the uploaded resume."
            );

        } catch (org.apache.tika.exception.TikaException e) {

            throw new InvalidFileException(
                    "Unable to parse the uploaded resume."
            );
        }
    }

    private void validateFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {

            throw new InvalidFileException(
                    "Resume file is required."
            );
        }

        if (file.getSize() > MAX_FILE_SIZE) {

            throw new InvalidFileException(
                    "Resume file must not exceed 5 MB."
            );
        }

        String fileName =
                file.getOriginalFilename();

        if (fileName == null || fileName.isBlank()) {

            throw new InvalidFileException(
                    "Resume file name is required."
            );
        }

        String lowerCaseFileName =
                fileName.toLowerCase();

        if (!lowerCaseFileName.endsWith(".pdf")
                && !lowerCaseFileName.endsWith(".docx")) {

            throw new InvalidFileException(
                    "Only PDF and DOCX resumes are supported."
            );
        }
    }

    private void validateContentType(
            String detectedType
    ) {

        boolean validPdf =
                "application/pdf".equalsIgnoreCase(
                        detectedType
                );

        boolean validDocx =
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                        .equalsIgnoreCase(detectedType);

        if (!validPdf && !validDocx) {

            throw new InvalidFileException(
                    "The uploaded file is not a valid PDF or DOCX document."
            );
        }
    }

    private String cleanExtractedText(
            String text
    ) {

        return text
                .replace("\r\n", "\n")
                .replace("\r", "\n")
                .replaceAll("[\\t ]+", " ")
                .replaceAll("\n{3,}", "\n\n")
                .trim();
    }
}