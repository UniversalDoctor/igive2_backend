package com.universaldoctor.igive2.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import javax.validation.constraints.*;

import java.io.Serializable;

import com.universaldoctor.igive2.domain.enumeration.QuestionType;

/**
 * A FormQuestion.
 */
@Document(collection = "form_question")
public class FormQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("question")
    private String question;

    @Field("is_mandatory")
    private Boolean isMandatory;

    @Field("type")
    private QuestionType type;

    @Field("options")
    private String options;

    @DBRef
    @Field("answer")
    @com.fasterxml.jackson.annotation.JsonBackReference
    private Answer answer;

    @DBRef
    @Field("form")
    @JsonIgnore
    @JsonIgnoreProperties("questions")
    private Form form;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public FormQuestion question(String question) {
        this.question = question;
        return this;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Boolean isIsMandatory() {
        return isMandatory;
    }

    public FormQuestion isMandatory(Boolean isMandatory) {
        this.isMandatory = isMandatory;
        return this;
    }

    public void setIsMandatory(Boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

    public QuestionType getType() {
        return type;
    }

    public FormQuestion type(QuestionType type) {
        this.type = type;
        return this;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public String getOptions() {
        return options;
    }

    public FormQuestion options(String options) {
        this.options = options;
        return this;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public Answer getAnswer() {
        return answer;
    }

    public FormQuestion answer(Answer answer) {
        this.answer = answer;
        return this;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public Form getForm() {
        return form;
    }

    public FormQuestion form(Form form) {
        this.form = form;
        return this;
    }

    public void setForm(Form form) {
        this.form = form;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FormQuestion)) {
            return false;
        }
        return id != null && id.equals(((FormQuestion) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "FormQuestion{" +
            "id=" + getId() +
            ", question='" + getQuestion() + "'" +
            ", isMandatory='" + isIsMandatory() + "'" +
            ", type='" + getType() + "'" +
            ", options='" + getOptions() + "'" +
            "}";
    }
}
