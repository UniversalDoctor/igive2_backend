package com.universaldoctor.igive2.service.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FormResult {
    String formId;
    String formName;
    String descriptionForm;
    ArrayList<ParticipantData> participantResponses=new ArrayList<>();

    public FormResult(String formId, String formName, String descriptionForm, ArrayList<ParticipantData> participantResponses) {
        this.formId = formId;
        this.formName = formName;
        this.descriptionForm = descriptionForm;
        this.participantResponses = participantResponses;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getDescriptionForm() {
        return descriptionForm;
    }

    public void setDescriptionForm(String descriptionForm) {
        this.descriptionForm = descriptionForm;
    }

    public ArrayList<ParticipantData> getParticipantResponses() {
        return participantResponses;
    }

    public void setParticipantResponses(ArrayList<ParticipantData> participantResponses) {
        this.participantResponses = participantResponses;
    }
}
