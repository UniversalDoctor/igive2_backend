package com.universaldoctor.igive2.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ResearcherDTO extends ManagedUserVM {

    public static final int PASSWORD_MIN_LENGTH = 4;

    public static final int PASSWORD_MAX_LENGTH = 100;

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    @NotNull
    private String country;
    @NotNull
    private String title;
    @NotNull
    private String institution;

    public ResearcherDTO(String country, String title, String institution) {
        this.country = country;
        this.title = title;
        this.institution = institution;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }
}
