package com.resumewise.resumeiq.service;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.resumewise.resumeiq.dto.ai.OptimizedResumeDto;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Renders the AI's structured OptimizedResumeDto into an actual downloadable
 * file. The AI never produces the file directly - this keeps formatting
 * consistent and fully under our control, using a single-column,
 * graphics-free layout (ATS-safe by design).
 */
@Service
public class ResumeExportService {

    public byte[] toDocx(OptimizedResumeDto resume) {
        try (XWPFDocument document = new XWPFDocument();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            addHeading(document, resume.fullName(), 20, true);

            if (resume.contactInfo() != null) {
                addParagraph(document, resume.contactInfo(), 10, false);
            }

            if (resume.summary() != null && !resume.summary().isBlank()) {
                addSectionTitle(document, "SUMMARY");
                addParagraph(document, resume.summary(), 11, false);
            }

            if (resume.skills() != null && !resume.skills().isEmpty()) {
                addSectionTitle(document, "SKILLS");
                addParagraph(
                        document,
                        String.join(" • ", resume.skills()),
                        11,
                        false
                );
            }

            if (resume.experience() != null && !resume.experience().isEmpty()) {
                addSectionTitle(document, "EXPERIENCE");

                for (OptimizedResumeDto.ExperienceEntry exp : resume.experience()) {

                    String heading =
                            safe(exp.title())
                                    + " — "
                                    + safe(exp.company());

                    if (exp.duration() != null && !exp.duration().isBlank()) {
                        heading += " (" + exp.duration() + ")";
                    }

                    addParagraph(
                            document,
                            heading,
                            11,
                            true
                    );

                    if (exp.bullets() != null) {
                        for (String bullet : exp.bullets()) {
                            addBullet(document, bullet);
                        }
                    }
                }
            }

            if (resume.projects() != null && !resume.projects().isEmpty()) {
                addSectionTitle(document, "PROJECTS");

                for (OptimizedResumeDto.ProjectEntry proj : resume.projects()) {

                    addParagraph(
                            document,
                            proj.title(),
                            11,
                            true
                    );

                    if (proj.bullets() != null) {
                        for (String bullet : proj.bullets()) {
                            addBullet(document, bullet);
                        }
                    }
                }
            }

            if (resume.education() != null && !resume.education().isEmpty()) {
                addSectionTitle(document, "EDUCATION");

                for (String edu : resume.education()) {
                    addParagraph(document, edu, 11, false);
                }
            }

            document.write(out);

            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to generate DOCX: " + e.getMessage(),
                    e
            );
        }
    }

    public byte[] toPdf(OptimizedResumeDto resume) {

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Document document =
                    new Document(
                            PageSize.A4,
                            40,
                            40,
                            40,
                            40
                    );

            PdfWriter.getInstance(document, out);

            document.open();

            Font nameFont =
                    new Font(
                            Font.HELVETICA,
                            18,
                            Font.BOLD
                    );

            Font contactFont =
                    new Font(
                            Font.HELVETICA,
                            10,
                            Font.NORMAL
                    );

            Font sectionFont =
                    new Font(
                            Font.HELVETICA,
                            13,
                            Font.BOLD
                    );

            Font bodyFont =
                    new Font(
                            Font.HELVETICA,
                            11,
                            Font.NORMAL
                    );

            Font boldBodyFont =
                    new Font(
                            Font.HELVETICA,
                            11,
                            Font.BOLD
                    );

            document.add(
                    new Paragraph(
                            safe(resume.fullName()),
                            nameFont
                    )
            );

            if (resume.contactInfo() != null) {
                document.add(
                        new Paragraph(
                                resume.contactInfo(),
                                contactFont
                        )
                );
            }

            document.add(Chunk.NEWLINE);

            // SUMMARY
            if (resume.summary() != null
                    && !resume.summary().isBlank()) {

                document.add(
                        new Paragraph(
                                "SUMMARY",
                                sectionFont
                        )
                );

                document.add(
                        new Paragraph(
                                resume.summary(),
                                bodyFont
                        )
                );

                document.add(Chunk.NEWLINE);
            }

            // SKILLS
            if (resume.skills() != null
                    && !resume.skills().isEmpty()) {

                document.add(
                        new Paragraph(
                                "SKILLS",
                                sectionFont
                        )
                );

                document.add(
                        new Paragraph(
                                String.join(
                                        " • ",
                                        resume.skills()
                                ),
                                bodyFont
                        )
                );

                document.add(Chunk.NEWLINE);
            }

            // EXPERIENCE
            if (resume.experience() != null
                    && !resume.experience().isEmpty()) {

                document.add(
                        new Paragraph(
                                "EXPERIENCE",
                                sectionFont
                        )
                );

                for (OptimizedResumeDto.ExperienceEntry exp
                        : resume.experience()) {

                    String heading =
                            safe(exp.title())
                                    + " — "
                                    + safe(exp.company());

                    if (exp.duration() != null
                            && !exp.duration().isBlank()) {

                        heading +=
                                " (" + exp.duration() + ")";
                    }

                    document.add(
                            new Paragraph(
                                    heading,
                                    boldBodyFont
                            )
                    );

                    if (exp.bullets() != null) {

                        for (String bullet : exp.bullets()) {

                            document.add(
                                    new Paragraph(
                                            "•  " + bullet,
                                            bodyFont
                                    )
                            );
                        }
                    }

                    document.add(Chunk.NEWLINE);
                }
            }

            // PROJECTS
            if (resume.projects() != null
                    && !resume.projects().isEmpty()) {

                document.add(
                        new Paragraph(
                                "PROJECTS",
                                sectionFont
                        )
                );

                for (OptimizedResumeDto.ProjectEntry proj
                        : resume.projects()) {

                    document.add(
                            new Paragraph(
                                    safe(proj.title()),
                                    boldBodyFont
                            )
                    );

                    if (proj.bullets() != null) {

                        for (String bullet : proj.bullets()) {

                            document.add(
                                    new Paragraph(
                                            "•  " + bullet,
                                            bodyFont
                                    )
                            );
                        }
                    }

                    document.add(Chunk.NEWLINE);
                }
            }

            // EDUCATION
            if (resume.education() != null
                    && !resume.education().isEmpty()) {

                document.add(
                        new Paragraph(
                                "EDUCATION",
                                sectionFont
                        )
                );

                for (String edu : resume.education()) {

                    document.add(
                            new Paragraph(
                                    edu,
                                    bodyFont
                            )
                    );
                }
            }

            document.close();

            return out.toByteArray();

        } catch (DocumentException | IOException e) {

            throw new RuntimeException(
                    "Failed to generate PDF: "
                            + e.getMessage(),
                    e
            );
        }
    }

    // ----------------------------------------------------
    // DOCX HELPERS
    // ----------------------------------------------------

    private void addHeading(
            XWPFDocument doc,
            String text,
            int size,
            boolean bold
    ) {

        XWPFParagraph paragraph =
                doc.createParagraph();

        XWPFRun run =
                paragraph.createRun();

        run.setText(
                text != null ? text : ""
        );

        run.setFontSize(size);
        run.setBold(bold);
    }

    private void addSectionTitle(
            XWPFDocument doc,
            String text
    ) {

        XWPFParagraph paragraph =
                doc.createParagraph();

        paragraph.setSpacingBefore(200);

        XWPFRun run =
                paragraph.createRun();

        run.setText(text);

        run.setBold(true);
        run.setFontSize(13);
    }

    private void addParagraph(
            XWPFDocument doc,
            String text,
            int size,
            boolean bold
    ) {

        XWPFParagraph paragraph =
                doc.createParagraph();

        XWPFRun run =
                paragraph.createRun();

        run.setText(
                text != null ? text : ""
        );

        run.setFontSize(size);
        run.setBold(bold);
    }

    private void addBullet(
            XWPFDocument doc,
            String text
    ) {

        XWPFParagraph paragraph =
                doc.createParagraph();

        paragraph.setIndentationLeft(300);

        XWPFRun run =
                paragraph.createRun();

        run.setText(
                "•  " + safe(text)
        );

        run.setFontSize(11);
    }

    private String safe(String value) {
        return value != null ? value : "";
    }
}

