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

import com.universaldoctor.igive2.domain.enumeration.State;

/**
 * A Study.
 */
@Document(collection = "study")
public class Study implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Size(max = 100)
    @Field("code")
    private String code;

    @Field("icon")
    private byte[] icon;

    @Field("icon_content_type")
    private String iconContentType;

    @NotNull
    @Size(max = 100)
    @Field("name")
    private String name;

    @Size(max = 1000)
    @Field("description")
    private String description;

    @Field("website")
    private String website;

    @Field("contact_email")
    private String contactEmail;

    @Field("start_date")
    private Instant startDate;

    @Field("end_date")
    private Instant endDate;

    @NotNull
    @Field("state")
    private State state;

    @Field("recruiting")
    private Boolean recruiting;

    @Field("requested_data")
    private String requestedData;

    @Field("data_justification")
    private String dataJustification;

    @DBRef
    @Field("institutions")
    private Set<ParticipantInstitution> institutions = new HashSet<>();

    @DBRef
    @Field("researcher")
    @JsonIgnore
    @JsonIgnoreProperties("studies")
    private Researcher researcher;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public Study code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public byte[] getIcon() {
        return icon;
    }

    public Study icon(byte[] icon) {
        this.icon = icon;
        return this;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    public String getIconContentType() {
        return iconContentType;
    }

    public Study iconContentType(String iconContentType) {
        this.iconContentType = iconContentType;
        return this;
    }

    public void setIconContentType(String iconContentType) {
        this.iconContentType = iconContentType;
    }

    public String getName() {
        return name;
    }

    public Study name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Study description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsite() {
        return website;
    }

    public Study website(String moreInfo) {
        this.website = moreInfo;
        return this;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public Study contactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
        return this;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public Study startDate(Instant startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public Study endDate(Instant endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public State getState() {
        return state;
    }

    public Study state(State state) {
        this.state = state;
        return this;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Boolean isRecruiting() {
        return recruiting;
    }

    public Study recruiting(Boolean recruiting) {
        this.recruiting = recruiting;
        return this;
    }

    public void setRecruiting(Boolean recruiting) {
        this.recruiting = recruiting;
    }

    public String getRequestedData() {
        return requestedData;
    }

    public Study requestedData(String requestedData) {
        this.requestedData = requestedData;
        return this;
    }

    public void setRequestedData(String requestedData) {
        this.requestedData = requestedData;
    }

    public String getDataJustification() {
        return dataJustification;
    }

    public Study dataJustification(String dataJustification) {
        this.dataJustification = dataJustification;
        return this;
    }

    public void setDataJustification(String dataJustification) {
        this.dataJustification = dataJustification;
    }

    public Set<ParticipantInstitution> getInstitutions() {
        return institutions;
    }

    public Study institutions(Set<ParticipantInstitution> participantInstitutions) {
        this.institutions = participantInstitutions;
        return this;
    }

    public Study addInstitutions(ParticipantInstitution participantInstitution) {
        this.institutions.add(participantInstitution);
        participantInstitution.setStudy(this);
        return this;
    }

    public Study removeInstitutions(ParticipantInstitution participantInstitution) {
        this.institutions.remove(participantInstitution);
        participantInstitution.setStudy(null);
        return this;
    }

    public void setInstitutions(Set<ParticipantInstitution> participantInstitutions) {
        this.institutions = participantInstitutions;
    }

    public Researcher getResearcher() {
        return researcher;
    }

    public Study researcher(Researcher researcher) {
        this.researcher = researcher;
        return this;
    }

    public void setResearcher(Researcher researcher) {
        this.researcher = researcher;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Study)) {
            return false;
        }
        return id != null && id.equals(((Study) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Study{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", icon='" + getIcon() + "'" +
            ", iconContentType='" + getIconContentType() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", moreInfo='" + getWebsite() + "'" +
            ", contactEmail='" + getContactEmail() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", state='" + getState() + "'" +
            ", recruiting='" + isRecruiting() + "'" +
            ", requestedData='" + getRequestedData() + "'" +
            ", dataJustification='" + getDataJustification() + "'" +
            "}";
    }
}
