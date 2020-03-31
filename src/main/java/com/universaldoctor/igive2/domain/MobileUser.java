package com.universaldoctor.igive2.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.universaldoctor.igive2.domain.enumeration.GenderType;

import com.universaldoctor.igive2.domain.enumeration.Diseases;

/**
 * A MobileUser.
 */
@Document(collection = "mobile_user")
public class MobileUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("gender")
    private GenderType gender;

    @Field("birthdate")
    private LocalDate birthdate;

    @NotNull
    @Field("diseases")
    private Diseases diseases;

    @NotNull
    @Field("user_id")
    private String userId;

    @Field("status")
    private String status;

    @Field("icon")
    private byte[] icon;

    @Field("icon_content_type")
    private String iconContentType;

    @Field("username")
    private String username;

    @DBRef
    @Field("iGive2User")
    private IGive2User iGive2User;

    @DBRef
    @Field("studies")
    private Set<Participant> studies = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GenderType getGender() {
        return gender;
    }

    public MobileUser gender(GenderType gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(GenderType gender) {
        this.gender = gender;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public MobileUser birthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
        return this;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public Diseases getDiseases() {
        return diseases;
    }

    public MobileUser diseases(Diseases diseases) {
        this.diseases = diseases;
        return this;
    }

    public void setDiseases(Diseases diseases) {
        this.diseases = diseases;
    }

    public String getUserId() {
        return userId;
    }

    public MobileUser userId(String userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public MobileUser status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public byte[] getIcon() {
        return icon;
    }

    public MobileUser icon(byte[] icon) {
        this.icon = icon;
        return this;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    public String getIconContentType() {
        return iconContentType;
    }

    public MobileUser iconContentType(String iconContentType) {
        this.iconContentType = iconContentType;
        return this;
    }

    public void setIconContentType(String iconContentType) {
        this.iconContentType = iconContentType;
    }

    public String getUsername() {
        return username;
    }

    public MobileUser username(String username) {
        this.username = username;
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public IGive2User getIGive2User() {
        return iGive2User;
    }

    public MobileUser iGive2User(IGive2User iGive2User) {
        this.iGive2User = iGive2User;
        return this;
    }

    public void setIGive2User(IGive2User iGive2User) {
        this.iGive2User = iGive2User;
    }

    public Set<Participant> getStudies() {
        return studies;
    }

    public MobileUser studies(Set<Participant> participants) {
        this.studies = participants;
        return this;
    }

    public MobileUser addStudies(Participant participant) {
        this.studies.add(participant);
        participant.setMobileUser(this);
        return this;
    }

    public MobileUser removeStudies(Participant participant) {
        this.studies.remove(participant);
        participant.setMobileUser(null);
        return this;
    }

    public void setStudies(Set<Participant> participants) {
        this.studies = participants;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MobileUser)) {
            return false;
        }
        return id != null && id.equals(((MobileUser) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "MobileUser{" +
            "id=" + getId() +
            ", gender='" + getGender() + "'" +
            ", birthdate='" + getBirthdate() + "'" +
            ", diseases='" + getDiseases() + "'" +
            ", userId='" + getUserId() + "'" +
            ", status='" + getStatus() + "'" +
            ", icon='" + getIcon() + "'" +
            ", iconContentType='" + getIconContentType() + "'" +
            ", username='" + getUsername() + "'" +
            "}";
    }
}
