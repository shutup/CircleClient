package com.shutup.circle.model.request;

/**
 * Created by shutup on 2017/4/11.
 */

public class QuestionAnswerCommentCreateRequest {
    private String comment;

    public QuestionAnswerCommentCreateRequest(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
