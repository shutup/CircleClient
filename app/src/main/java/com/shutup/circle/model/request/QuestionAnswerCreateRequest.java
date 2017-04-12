package com.shutup.circle.model.request;

/**
 * Created by shutup on 2017/4/7.
 */

public class QuestionAnswerCreateRequest {
    private String answer;

    public QuestionAnswerCreateRequest(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
