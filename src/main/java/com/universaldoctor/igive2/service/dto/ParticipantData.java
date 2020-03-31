package com.universaldoctor.igive2.service.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ParticipantData {
    String anonymousId;
    ArrayList<QuestionsAnswers> responses=new ArrayList<>();

    public ParticipantData(String anonymousId,  ArrayList<QuestionsAnswers> responses) {
        this.anonymousId = anonymousId;
        this.responses = responses;
    }

    public String getAnonymousId() {
        return anonymousId;
    }

    public void setAnonymousId(String anonymousId) {
        this.anonymousId = anonymousId;
    }


    public ArrayList<QuestionsAnswers> getResponses() {
        return responses;
    }

    public void setResponses(ArrayList<QuestionsAnswers> responses) {
        this.responses = responses;
    }
}
