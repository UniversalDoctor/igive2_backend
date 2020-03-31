package com.universaldoctor.igive2.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

import com.universaldoctor.igive2.domain.enumeration.DataType;

/**
 * A Data.
 */
@Document(collection = "data")
public class Data implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("data")
    private DataType data;

    @Field("notes")
    private String notes;

    @Field("date")
    private Instant date;

    @NotNull
    @Field("value")
    private String value;

    @DBRef
    @Field("mobileUser")
    @JsonIgnore
    private MobileUser mobileUser;


    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DataType getData() {
        return data;
    }

    public Data data(DataType data) {
        this.data = data;
        return this;
    }

    public void setData(DataType data) {
        this.data = data;
    }

    public String getNotes() {
        return notes;
    }

    public Data notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getDate() {
        return date;
    }

    public Data date(Instant date) {
        this.date = date;
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public Data value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public MobileUser getMobileUser() {
        return mobileUser;
    }

    public Data mobileUser(MobileUser mobileUser) {
        this.mobileUser = mobileUser;
        return this;
    }

    public void setMobileUser(MobileUser mobileUser) {
        this.mobileUser = mobileUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Data)) {
            return false;
        }
        return id != null && id.equals(((Data) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Data{" +
            "id=" + getId() +
            ", data='" + getData() + "'" +
            ", notes='" + getNotes() + "'" +
            ", date='" + getDate() + "'" +
            ", value='" + getValue() + "'" +
            "}";
    }
}
