package com.universaldoctor.igive2.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A FormAnswers.
 */
@Document(collection = "form_answers")
public class FormAnswers implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("saved_date")
    private Instant savedDate;

    @Field("completed")
    private Boolean completed;

    @DBRef
    @JsonIgnore
    @Field("form")
    private Form form;

    @DBRef
    @Field("responses")
    private Set<Answer> responses = new HashSet<>();

    @DBRef
    @Field("participant")
    @JsonIgnore
    private Participant participant;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getSavedDate() {
        return savedDate;
    }

    public FormAnswers savedDate(Instant savedDate) {
        this.savedDate = savedDate;
        return this;
    }

    public void setSavedDate(Instant savedDate) {
        this.savedDate = savedDate;
    }

    public Boolean isCompleted() {
        return completed;
    }

    public FormAnswers completed(Boolean completed) {
        this.completed = completed;
        return this;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Form getForm() {
        return form;
    }

    public FormAnswers form(Form form) {
        this.form = form;
        return this;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public Set<Answer> getResponses() {
        return responses;
    }

    public FormAnswers responses(Set<Answer> answers) {
        this.responses = answers;
        return this;
    }

    public FormAnswers addResponses(Answer answer) {
        this.responses.add(answer);
        answer.setFormAnswers(this);
        return this;
    }

    public FormAnswers removeResponses(Answer answer) {
        this.responses.remove(answer);
        answer.setFormAnswers(null);
        return this;
    }

    public void setResponses(Set<Answer> answers) {
        this.responses = answers;
    }

    public Participant getParticipant() {
        return participant;
    }

    public FormAnswers participant(Participant participant) {
        this.participant = participant;
        return this;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FormAnswers)) {
            return false;
        }
        return id != null && id.equals(((FormAnswers) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "FormAnswers{" +
            "id=" + getId() +
            ", savedDate='" + getSavedDate() + "'" +
            ", completed='" + isCompleted() + "'" +
            "}";
    }
}
