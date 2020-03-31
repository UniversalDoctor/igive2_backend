package com.universaldoctor.igive2.service.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class StudyParticipantManage {
    private String anonymousParticipantId;
    private int percentage;
    private ArrayList<DataMeanDTO> dataMean=new ArrayList<>();

    public StudyParticipantManage( String anonymousParticipantId, int percentage,ArrayList<DataMeanDTO> dataMean) {
        this.anonymousParticipantId = anonymousParticipantId;
        this.percentage = percentage;
        this.dataMean = dataMean;
    }

    public String getAnonymousParticipantId() {
        return anonymousParticipantId;
    }

    public void setAnonymousParticipantId(String anonymousParticipantId) {
        this.anonymousParticipantId = anonymousParticipantId;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public ArrayList<DataMeanDTO> getDataMean() {
        return dataMean;
    }

    public void setDataMean(ArrayList<DataMeanDTO> dataMean) {
        this.dataMean = dataMean;
    }
}
