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
 * A ParticipantInvitation.
 */
@Document(collection = "participant_invitation")
public class ParticipantInvitation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("email")
    private String email;

    @Field("state")
    private Boolean state;

    @JsonIgnore
    @Field("participant_id")
    private String participantId;

    @DBRef
    @Field("study")
    @JsonIgnore
    @JsonIgnoreProperties("invitations")
    private Study study;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public ParticipantInvitation email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean isState() {
        return state;
    }

    public ParticipantInvitation state(Boolean state) {
        this.state = state;
        return this;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getParticipantId() {
        return participantId;
    }

    public ParticipantInvitation participantId(String participantId) {
        this.participantId = participantId;
        return this;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public Study getStudy() {
        return study;
    }

    public ParticipantInvitation study(Study study) {
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
        if (!(o instanceof ParticipantInvitation)) {
            return false;
        }
        return id != null && id.equals(((ParticipantInvitation) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ParticipantInvitation{" +
            "id=" + getId() +
            ", email='" + getEmail() + "'" +
            ", state='" + isState() + "'" +
            ", participantId='" + getParticipantId() + "'" +
            "}";
    }
}
