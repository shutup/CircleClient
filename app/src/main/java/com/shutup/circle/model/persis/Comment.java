package com.shutup.circle.model.persis;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by shutup on 2017/4/8.
 */

public class Comment extends RealmObject{

    /**
     * id : 3
     * comment : thanks
     * user : {"username":"tom","createdAt":1491212616759,"updatedAt":1491212616759,"id":1}
     * createdAt : 1491212994760
     * updatedAt : 1491212994760
     * agreedUsers : []
     * disagreedUsers : []
     * comments : []
     */
    @PrimaryKey
    private int id;
    private String comment;
    private User user;
    private User replyUser;
    private boolean reply;
    private long createdAt;
    private long updatedAt;
    private RealmList<User> agreedUsers;
    private RealmList<User> disagreedUsers;

    public Comment(){}
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
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

    public User getReplyUser() {
        return replyUser;
    }

    public void setReplyUser(User replyUser) {
        this.replyUser = replyUser;
    }

    public boolean isReply() {
        return reply;
    }

    public void setReply(boolean reply) {
        this.reply = reply;
    }
}
