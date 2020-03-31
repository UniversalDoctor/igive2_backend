package com.universaldoctor.igive2.service.dto;

import com.universaldoctor.igive2.domain.enumeration.State;

public class FormDTO {

    private String idForm;
    private String name;
    private String description;
    private State state;
    private int numberOfQuestions;
    private int numberOfAnswered;

    public FormDTO(String idForm, String name, String description, State state, int numberOfQuestions, int numberOfAnswered) {
        this.idForm = idForm;
        this.name = name;
        this.description = description;
        this.state = state;
        this.numberOfQuestions = numberOfQuestions;
        this.numberOfAnswered = numberOfAnswered;
    }

    public String getIdForm() {
        return idForm;
    }

    public void setIdForm(String idForm) {
        this.idForm = idForm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }

    public int getNumberOfAnswered() {
        return numberOfAnswered;
    }

    public void setNumberOfAnswered(int numberOfAnswered) {
        this.numberOfAnswered = numberOfAnswered;
    }
}
