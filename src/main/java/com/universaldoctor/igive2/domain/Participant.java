package com.universaldoctor.igive2.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Participant.
 */
@Document(collection = "participant")
public class Participant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("entry_date")
    private @NotNull Instant entryDate;

    @NotNull
    @Field("anonymous_id")
    private String anonymousId;

    @DBRef
    @Field("participantData")
    private Set<Data> participantData = new HashSet<>();

    @DBRef
    @Field("mobileUser")
    @JsonIgnore
    private MobileUser mobileUser;

    @DBRef
    @Field("study")
    @JsonIgnoreProperties("participants")
    private Study study;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public @NotNull Instant getEntryDate() {
        return entryDate;
    }

    public Participant entryDate(@NotNull Instant entryDate) {
        this.entryDate = entryDate;
        return this;
    }

    public void setEntryDate(@NotNull Instant entryDate) {
        this.entryDate = entryDate;
    }

    public String getAnonymousId() {
        return anonymousId;
    }

    public Participant anonymousId(String anonymousId) {
        this.anonymousId = anonymousId;
        return this;
    }

    public void setAnonymousId(String anonymousId) {
        this.anonymousId = anonymousId;
    }

    public Set<Data> getParticipantData() {
        return participantData;
    }

    public Participant participantData(Set<Data> data) {
        this.participantData = data;
        return this;
    }

    public Participant addParticipantData(Data data) {
        this.participantData.add(data);
        return this;
    }

    public Participant removeParticipantData(Data data) {
        this.participantData.remove(data);
        return this;
    }

    public void setParticipantData(Set<Data> data) {
        this.participantData = data;
    }

    public MobileUser getMobileUser() {
        return mobileUser;
    }

    public Participant mobileUser(MobileUser mobileUser) {
        this.mobileUser = mobileUser;
        return this;
    }

    public void setMobileUser(MobileUser mobileUser) {
        this.mobileUser = mobileUser;
    }

    public Study getStudy() {
        return study;
    }

    public Participant study(Study study) {
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
        if (!(o instanceof Participant)) {
            return false;
        }
        return id != null && id.equals(((Participant) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Participant{" +
            "id=" + getId() +
            ", entryDate='" + getEntryDate() + "'" +
            ", anonymousId='" + getAnonymousId() + "'" +
            "}";
    }
}
