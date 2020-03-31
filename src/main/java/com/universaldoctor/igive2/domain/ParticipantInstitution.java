package com.universaldoctor.igive2.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serializable;

/**
 * A ParticipantInstitution.
 */
@Document(collection = "participant_institution")
public class ParticipantInstitution implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("logo")
    private byte[] logo;

    @Field("logo_content_type")
    private String logoContentType;

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

    public byte[] getLogo() {
        return logo;
    }

    public ParticipantInstitution logo(byte[] logo) {
        this.logo = logo;
        return this;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoContentType() {
        return logoContentType;
    }

    public ParticipantInstitution logoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
        return this;
    }

    public void setLogoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
    }

    public Study getStudy() {
        return study;
    }

    public ParticipantInstitution study(Study study) {
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
        if (!(o instanceof ParticipantInstitution)) {
            return false;
        }
        return id != null && id.equals(((ParticipantInstitution) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ParticipantInstitution{" +
            "id=" + getId() +
            ", logo='" + getLogo() + "'" +
            ", logoContentType='" + getLogoContentType() + "'" +
            "}";
    }
}
