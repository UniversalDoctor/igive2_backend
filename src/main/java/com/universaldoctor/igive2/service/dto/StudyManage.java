package com.universaldoctor.igive2.service.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class StudyManage {

    private StudyDTO studyDTO;
    private ArrayList<StudyParticipantManage> studyParticipantManage = new ArrayList<>();

    public StudyManage(StudyDTO studyDTO, ArrayList<StudyParticipantManage> studyParticipantManage) {
        this.studyDTO = studyDTO;
        this.studyParticipantManage = studyParticipantManage;
    }

    public StudyDTO getStudyDTO() {
        return studyDTO;
    }

    public void setStudyDTO(StudyDTO studyDTO) {
        this.studyDTO = studyDTO;
    }

    public ArrayList<StudyParticipantManage> getStudyParticipantManage() {
        return studyParticipantManage;
    }

    public void setStudyParticipantManage(ArrayList<StudyParticipantManage> studyParticipantManage) {
        this.studyParticipantManage = studyParticipantManage;
    }
}
