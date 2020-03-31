package com.universaldoctor.igive2.service.dto;

import com.universaldoctor.igive2.domain.Form;
import com.universaldoctor.igive2.domain.Study;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ParticipantAnswers {

    private String participantId;
    private String formId;
    private ArrayList<AnswerMobile> responses = new ArrayList<>();

    public ParticipantAnswers(String participantId, String formId) {
        this.participantId = participantId;
        this.formId=formId;

    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public ArrayList<AnswerMobile> getResponses() {
        return responses;
    }

    public void setResponses(ArrayList<AnswerMobile> responses) {
        this.responses = responses;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public ParticipantAnswers responses(ArrayList<AnswerMobile> responses) {
        this.responses = responses;
        return this;
    }

    public ParticipantAnswers addResponse(AnswerMobile response) {
        this.responses.add(response);
        return this;
    }

    public ParticipantAnswers removeResponse(AnswerMobile response) {
        this.responses.remove(response);
        return this;
    }

}
