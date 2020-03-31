package com.universaldoctor.igive2.service.dto;

import com.universaldoctor.igive2.domain.Data;
import com.universaldoctor.igive2.domain.enumeration.Diseases;
import com.universaldoctor.igive2.domain.enumeration.GenderType;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class SetUp {

    private String name;
    private String firstName;
    private String lastName;
    private String email;

    @NotNull
    private String country;
    @NotNull
    private GenderType gender;
    @NotNull
    private LocalDate birthdate;
    @NotNull
    private Diseases diseases;
    private String weight;
    private String height;
    private int numberStudies;
    private boolean newsLetter;

    public SetUp(String name, String firstName, String lastName, String email, @NotNull String country,
                 @NotNull GenderType gender, @NotNull LocalDate birthdate, @NotNull Diseases diseases, String weight,
                 String height, int numberStudies, boolean newsLetter) {
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.country = country;
        this.gender = gender;
        this.birthdate = birthdate;
        this.diseases = diseases;
        this.weight = weight;
        this.height = height;
        this.numberStudies = numberStudies;
        this.newsLetter = newsLetter;
    }

    public int getNumberStudies() {
        return numberStudies;
    }

    public void setNumberStudies(int numberStudies) {
        this.numberStudies = numberStudies;
    }

    public boolean isNewsLetter() {
        return newsLetter;
    }

    public void setNewsLetter(boolean newsLetter) {
        this.newsLetter = newsLetter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public Diseases getDiseases() {
        return diseases;
    }

    public void setDiseases(Diseases diseases) {
        this.diseases = diseases;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public GenderType getGender() {
        return gender;
    }

    public void setGender(GenderType gender) {
        this.gender = gender;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
