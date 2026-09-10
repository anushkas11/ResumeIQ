package com.resumewise.resumeiq.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_descriptions")
public class JobDescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String companyName;

    private String roleTitle;

    @Lob
    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String rawText;

    private LocalDateTime createdAt;

    public JobDescription() {
    }

    public JobDescription(User user, String companyName, String roleTitle, String rawText) {
        this.user = user;
        this.companyName = companyName;
        this.roleTitle = roleTitle;
        this.rawText = rawText;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getRoleTitle() {
        return roleTitle;
    }

    public void setRoleTitle(String roleTitle) {
        this.roleTitle = roleTitle;
    }

    public String getRawText() {
        return rawText;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
