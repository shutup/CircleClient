package com.shutup.circle.model.response;

/**
 * Created by shutup on 2016/12/16.
 */

public class RestInfo {
    private String msg;
    private boolean isSuccess;

    public RestInfo(String msg, boolean isSuccess) {
        this.msg = msg;
        this.isSuccess = isSuccess;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
