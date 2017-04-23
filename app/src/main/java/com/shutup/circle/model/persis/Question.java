package com.shutup.circle.model.persis;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by shutup on 2017/4/8.
 */
public class Question extends RealmObject{
    /**
     * id : 1
     * question : 1+1=?
     * answers : []
     * user : {"username":"tom","createdAt":1491212616759,"updatedAt":1491212616759,"id":1}
     * createdAt : 1491212674504
     * updatedAt : 1491212674504
     * agreedUsers : []
     * disagreedUsers : []
     */
    @PrimaryKey
    private Long id;
    private String question;
    private User user;
    private Date createdAt;
    private Date updatedAt;
    private RealmList<Answer> answers;
    private RealmList<User> agreedUsers;
    private RealmList<User> disagreedUsers;

    public Question(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
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

    public RealmList<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(RealmList<Answer> answers) {
        this.answers = answers;
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
