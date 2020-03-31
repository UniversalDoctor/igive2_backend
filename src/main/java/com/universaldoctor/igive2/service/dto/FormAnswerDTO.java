package com.universaldoctor.igive2.service.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FormAnswerDTO {
    String idForm;
    String nameForm;
    String descriptionForm;
    ArrayList<QuestionsAnswers> responses=new ArrayList<>();

    public FormAnswerDTO(String idForm, String nameForm, String descriptionForm, ArrayList<QuestionsAnswers> responses) {
        this.idForm = idForm;
        this.nameForm = nameForm;
        this.descriptionForm = descriptionForm;
        this.responses = responses;
    }

    public String getIdForm() {
        return idForm;
    }

    public void setIdForm(String idForm) {
        this.idForm = idForm;
    }

    public ArrayList<QuestionsAnswers> getResponses() {
        return responses;
    }

    public void setResponses(ArrayList<QuestionsAnswers> responses) {
        this.responses = responses;
    }

    public String getNameForm() {
        return nameForm;
    }

    public void setNameForm(String nameForm) {
        this.nameForm = nameForm;
    }

    public String getDescriptionForm() {
        return descriptionForm;
    }

    public void setDescriptionForm(String descriptionForm) {
        this.descriptionForm = descriptionForm;
    }
}
