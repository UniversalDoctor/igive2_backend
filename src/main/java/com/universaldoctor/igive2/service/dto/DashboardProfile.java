package com.universaldoctor.igive2.service.dto;

public class DashboardProfile {
    String firstName;
    String lastName;
    String title;
    String email;
    String institution;
    String country;
    int publishedStudies;

    public DashboardProfile(String firstName, String lastName,
                            String title, String email, String institution, String country, int publishedStudies) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
        this.email = email;
        this.institution=institution;
        this.country = country;
        this.publishedStudies = publishedStudies;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getPublishedStudies() {
        return publishedStudies;
    }

    public void setPublishedStudies(int publishedStudies) {
        this.publishedStudies = publishedStudies;
    }
}
