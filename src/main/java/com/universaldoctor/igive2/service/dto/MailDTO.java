package com.universaldoctor.igive2.service.dto;

public class MailDTO {
    private String studyId;
    private String researcherMail;
    private String subject;
    private String body;

    public MailDTO(String studyId, String researcherMail, String subject, String body) {
        this.studyId = studyId;
        this.researcherMail = researcherMail;
        this.subject = subject;
        this.body = body;
    }

    public String getResearcherMail() {
        return researcherMail;
    }

    public void setResearcherMail(String researcherMail) {
        this.researcherMail = researcherMail;
    }

    public String getStudyId() {
        return studyId;
    }

    public void setStudyId(String studyId) {
        this.studyId = studyId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
