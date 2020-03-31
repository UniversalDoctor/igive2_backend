package com.universaldoctor.igive2.service.dto;

import com.universaldoctor.igive2.domain.enumeration.State;

import java.time.Instant;

public class StudyDTO {
    private String id;
    private String name;
    private String code;
    private byte[] icon;
    private Instant startDate;
    private Instant endDate;
    private Boolean recruiting;
    private String contactEmail;
    private String website;
    private State state;
    private String description;
    private String dataJustification;
    private String requestedData;
    private int activeParticipants;

    public StudyDTO(String id, String name, String code, byte[] icon, Instant startDate, Instant endDate,
                    Boolean recruiting, String contactEmail, String website, State state, String description,
                    String dataJustification, String requestedData, int activeParticipants) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.icon = icon;
        this.startDate = startDate;
        this.endDate = endDate;
        this.recruiting = recruiting;
        this.contactEmail = contactEmail;
        this.website = website;
        this.state = state;
        this.description = description;
        this.dataJustification=dataJustification;
        this.requestedData = requestedData;
        this.activeParticipants = activeParticipants;
    }

    public String getDataJustification() {
        return dataJustification;
    }

    public void setDataJustification(String dataJustification) {
        this.dataJustification = dataJustification;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public byte[] getIcon() {
        return icon;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Boolean getRecruiting() {
        return recruiting;
    }

    public void setRecruiting(Boolean recruiting) {
        this.recruiting = recruiting;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getActiveParticipants() {
        return activeParticipants;
    }

    public void setActiveParticipants(int activeParticipants) {
        this.activeParticipants = activeParticipants;
    }

    public String getRequestedData() {
        return requestedData;
    }

    public void setRequestedData(String requestedData) {
        this.requestedData = requestedData;
    }
}
