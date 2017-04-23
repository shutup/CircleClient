package com.shutup.circle.model.persis;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by shutup on 2017/4/8.
 */

public class Answer extends RealmObject{

    @PrimaryKey
    private Long id;
    private String answer;
    private User user;
    private Date createdAt;
    private Date updatedAt;
    private RealmList<Comment> comments;
    private RealmList<User> agreedUsers;
    private RealmList<User> disagreedUsers;


    public Answer(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public RealmList<Comment> getComments() {
        return comments;
    }

    public void setComments(RealmList<Comment> comments) {
        this.comments = comments;
    }

    public RealmList<User> getAgreedUsers() {
        return agreedUsers;
    }

    public void setAgreedUsers(RealmList<User> agreedUsers) {
        this.agreedUsers = agreedUsers;
    }

    public RealmList<User> getDisagreedUsers() {
        return disagreedUsers;
    }

    public void setDisagreedUsers(RealmList<User> disagreedUsers) {
        this.disagreedUsers = disagreedUsers;
    }
}
