package com.resumewise.resumeiq.dto.request;

import jakarta.validation.constraints.NotBlank;

public class JobDescriptionCreateRequest {

    private String companyName;

    private String roleTitle;

    @NotBlank(message = "Job description text must not be blank")
    private String rawText;

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
}
