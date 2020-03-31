package com.universaldoctor.igive2.service.dto;

public class AnswerMobile {

    private String questionId;
    private String response;

    public AnswerMobile(String questionId, String response) {
        this.questionId = questionId;
        this.response = response;
    }
    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

}
