package com.universaldoctor.igive2.service.dto;

import com.universaldoctor.igive2.domain.enumeration.State;

import java.time.Instant;

public class ParticipantDTO {
    private String idParticipant;
    private String anonymousIdParticipant;
    private Instant entryDateParticipant;
    private String idStudy;
    private String codeStudy;
    private String nameStudy;
    private Boolean dataCollect;
    private String descriptionStudy;
    private String dataJustification;
    private State stateStudy;
    private Instant startDateStudy;
    private Instant endDateStudy;
    private byte[] iconStudy;

    public ParticipantDTO(String idParticipant, String anonymousIdParticipant, Instant entryDateParticipant, String idStudy, Boolean dataCollect,
                          String codeStudy, String nameStudy, String descriptionStudy, String dataJustification, State stateStudy, Instant startDateStudy,
                          Instant endDateStudy, byte[] iconStudy) {
        this.idParticipant = idParticipant;
        this.anonymousIdParticipant = anonymousIdParticipant;
        this.entryDateParticipant = entryDateParticipant;
        this.idStudy = idStudy;
        this.codeStudy = codeStudy;
        this.nameStudy = nameStudy;
        this.dataCollect=dataCollect;
        this.descriptionStudy = descriptionStudy;
        this.dataJustification=dataJustification;
        this.stateStudy = stateStudy;
        this.startDateStudy = startDateStudy;
        this.endDateStudy = endDateStudy;
        this.iconStudy=iconStudy;
    }

    public String getDataJustification() {
        return dataJustification;
    }

    public void setDataJustification(String dataJustification) {
        this.dataJustification = dataJustification;
    }

    public Boolean getDataCollect() {
        return dataCollect;
    }

    public void setDataCollect(Boolean dataCollect) {
        this.dataCollect = dataCollect;
    }

    public byte[] getIconStudy() {
        return iconStudy;
    }

    public void setIconStudy(byte[] iconStudy) {
        this.iconStudy = iconStudy;
    }

    public String getIdParticipant() {
        return idParticipant;
    }

    public void setIdParticipant(String idParticipant) {
        this.idParticipant = idParticipant;
    }

    public String getAnonymousIdParticipant() {
        return anonymousIdParticipant;
    }

    public void setAnonymousIdParticipant(String anonymousIdParticipant) {
        this.anonymousIdParticipant = anonymousIdParticipant;
    }

    public Instant getEntryDateParticipant() {
        return entryDateParticipant;
    }

    public void setEntryDateParticipant(Instant entryDateParticipant) {
        this.entryDateParticipant = entryDateParticipant;
    }

    public String getIdStudy() {
        return idStudy;
    }

    public void setIdStudy(String idStudy) {
        this.idStudy = idStudy;
    }

    public String getCodeStudy() {
        return codeStudy;
    }

    public void setCodeStudy(String codeStudy) {
        this.codeStudy = codeStudy;
    }

    public String getNameStudy() {
        return nameStudy;
    }

    public void setNameStudy(String nameStudy) {
        this.nameStudy = nameStudy;
    }

    public String getDescriptionStudy() {
        return descriptionStudy;
    }

    public void setDescriptionStudy(String descriptionStudy) {
        this.descriptionStudy = descriptionStudy;
    }

    public State getStateStudy() {
        return stateStudy;
    }

    public void setStateStudy(State stateStudy) {
        this.stateStudy = stateStudy;
    }

    public Instant getStartDateStudy() {
        return startDateStudy;
    }

    public void setStartDateStudy(Instant startDateStudy) {
        this.startDateStudy = startDateStudy;
    }

    public Instant getEndDateStudy() {
        return endDateStudy;
    }

    public void setEndDateStudy(Instant endDateStudy) {
        this.endDateStudy = endDateStudy;
    }
}
