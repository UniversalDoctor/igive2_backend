package com.universaldoctor.igive2.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.universaldoctor.igive2.domain.enumeration.State;

/**
 * A Form.
 */
@Document(collection = "form")
public class Form implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Size(max = 100)
    @Field("name")
    private String name;

    @NotNull
    @Size(max = 500)
    @Field("description")
    private String description;

    @Field("state")
    private State state;

    @DBRef
    @Field("questions")
    private Set<FormQuestion> questions = new HashSet<>();

    @DBRef
    @Field("study")
    @JsonIgnore
    private Study study;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Form name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Form description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public State getState() {
        return state;
    }

    public Form state(State state) {
        this.state = state;
        return this;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Set<FormQuestion> getQuestions() {
        return questions;
    }

    public Form questions(Set<FormQuestion> formQuestions) {
        this.questions = formQuestions;
        return this;
    }

    public Form addQuestions(FormQuestion formQuestion) {
        this.questions.add(formQuestion);
        formQuestion.setForm(this);
        return this;
    }

    public Form removeQuestions(FormQuestion formQuestion) {
        this.questions.remove(formQuestion);
        formQuestion.setForm(null);
        return this;
    }

    public void setQuestions(Set<FormQuestion> formQuestions) {
        this.questions = formQuestions;
    }

    public Study getStudy() {
        return study;
    }

    public Form study(Study study) {
        this.study = study;
        return this;
    }

    public void setStudy(Study study) {
        this.study = study;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Form)) {
            return false;
        }
        return id != null && id.equals(((Form) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Form{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", state='" + getState() + "'" +
            "}";
    }
}
