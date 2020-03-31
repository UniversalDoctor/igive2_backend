package com.universaldoctor.igive2.domain;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Researcher.
 */
@Document(collection = "researcher")
public class Researcher implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("institution")
    private String institution;

    @NotNull
    @Field("honorifics")
    private String honorifics;

    @NotNull
    @Field("user_id")
    private String userId;

    @DBRef
    @Field("iGive2User")
    private IGive2User iGive2User;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInstitution() {
        return institution;
    }

    public Researcher institution(String institution) {
        this.institution = institution;
        return this;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getHonorifics() {
        return honorifics;
    }

    public Researcher honorifics(String honorifics) {
        this.honorifics = honorifics;
        return this;
    }

    public void setHonorifics(String honorifics) {
        this.honorifics = honorifics;
    }

    public String getUserId() {
        return userId;
    }

    public Researcher userId(String userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public IGive2User getIGive2User() {
        return iGive2User;
    }

    public Researcher iGive2User(IGive2User iGive2User) {
        this.iGive2User = iGive2User;
        return this;
    }

    public void setIGive2User(IGive2User iGive2User) {
        this.iGive2User = iGive2User;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Researcher)) {
            return false;
        }
        return id != null && id.equals(((Researcher) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Researcher{" +
            "id=" + getId() +
            ", institution='" + getInstitution() + "'" +
            ", honorifics='" + getHonorifics() + "'" +
            ", userId='" + getUserId() + "'" +
            "}";
    }
}
