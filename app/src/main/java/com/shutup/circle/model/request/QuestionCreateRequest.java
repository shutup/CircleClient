package com.shutup.circle.model.request;

/**
 * Created by shutup on 2017/4/7.
 */

public class QuestionCreateRequest {
    private String question;

    public QuestionCreateRequest(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
