package com.universaldoctor.igive2.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Answer.
 */
@Document(collection = "answer")
public class Answer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("data")
    private String data;

    @DBRef
    @Field("formQuestion")
    private FormQuestion formQuestion;

    @DBRef
    @Field("formAnswers")
    @JsonIgnore
    private FormAnswers formAnswers;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public Answer data(String data) {
        this.data = data;
        return this;
    }

    public void setData(String data) {
        this.data = data;
    }

    public FormQuestion getFormQuestion() {
        return formQuestion;
    }

    public Answer formQuestion(FormQuestion formQuestion) {
        this.formQuestion = formQuestion;
        return this;
    }

    public void setFormQuestion(FormQuestion formQuestion) {
        this.formQuestion = formQuestion;
    }

    public FormAnswers getFormAnswers() {
        return formAnswers;
    }

    public Answer formAnswers(FormAnswers formAnswers) {
        this.formAnswers = formAnswers;
        return this;
    }

    public void setFormAnswers(FormAnswers formAnswers) {
        this.formAnswers = formAnswers;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Answer)) {
            return false;
        }
        return id != null && id.equals(((Answer) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Answer{" +
            "id=" + getId() +
            ", data='" + getData() + "'" +
            "}";
    }
}
