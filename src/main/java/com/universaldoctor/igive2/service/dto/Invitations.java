package com.universaldoctor.igive2.service.dto;

import com.universaldoctor.igive2.domain.ParticipantInvitation;
import com.universaldoctor.igive2.domain.Researcher;
import com.universaldoctor.igive2.domain.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Invitations {
    String studyId;
    Researcher researcher;
    User user;
    ArrayList<ParticipantInvitation> invitations=new ArrayList<>();

    public Invitations(String studyId, Researcher researcher, User user, ArrayList<ParticipantInvitation> invitations) {
        this.studyId = studyId;
        this.researcher = researcher;
        this.user = user;
        this.invitations = invitations;
    }

    public Researcher getResearcher() {
        return researcher;
    }

    public void setResearcher(Researcher researcher) {
        this.researcher = researcher;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStudyId() {
        return studyId;
    }

    public void setStudyId(String studyId) {
        this.studyId = studyId;
    }

    public ArrayList<ParticipantInvitation> getInvitations() {
        return invitations;
    }

    public void setInvitations(ArrayList<ParticipantInvitation> invitations) {
        this.invitations = invitations;
    }
}
