package com.universaldoctor.igive2.service.dto;

import com.universaldoctor.igive2.domain.enumeration.QuestionType;

public class QuestionsAnswers {
    String idQuestion;
    QuestionType type;
    String questions;
    String options;
    String idAnswer;
    String answer;

    public QuestionsAnswers(String idQuestion, QuestionType type, String questions, String options, String idAnswer, String answer) {
        this.idQuestion = idQuestion;
        this.type = type;
        this.questions = questions;
        this.options = options;
        this.idAnswer = idAnswer;
        this.answer = answer;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public String getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(String idQuestion) {
        this.idQuestion = idQuestion;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public String getIdAnswer() {
        return idAnswer;
    }

    public void setIdAnswer(String idAnswer) {
        this.idAnswer = idAnswer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
