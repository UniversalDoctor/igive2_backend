package com.universaldoctor.igive2.domain;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A IGive2User.
 */
@Document(collection = "i_give_2_user")
public class IGive2User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("newsletter")
    private Boolean newsletter;

    @NotNull
    @Field("terms_accepted")
    private Boolean termsAccepted;

    @NotNull
    @Field("country")
    private String country;


    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean isNewsletter() {
        return newsletter;
    }

    public IGive2User newsletter(Boolean newsletter) {
        this.newsletter = newsletter;
        return this;
    }

    public void setNewsletter(Boolean newsletter) {
        this.newsletter = newsletter;
    }

    public Boolean isTermsAccepted() {
        return termsAccepted;
    }

    public IGive2User termsAccepted(Boolean termsAccepted) {
        this.termsAccepted = termsAccepted;
        return this;
    }

    public void setTermsAccepted(Boolean termsAccepted) {
        this.termsAccepted = termsAccepted;
    }

    public String getCountry() {
        return country;
    }

    public IGive2User country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IGive2User)) {
            return false;
        }
        return id != null && id.equals(((IGive2User) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return
            "IGive2User{" +
            "id=" + getId() +
            ", newsletter='" + isNewsletter() + "'" +
            ", termsAccepted='" + isTermsAccepted() + "'" +
            ", country='" + getCountry() + "'" +
            "}";
    }
}
