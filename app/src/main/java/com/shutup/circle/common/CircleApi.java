package com.shutup.circle.common;

import com.shutup.circle.model.request.LoginUserRequest;
import com.shutup.circle.model.request.RegisterUserRequest;
import com.shutup.circle.model.response.RestInfo;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by shutup on 2017/4/4.
 */

public interface CircleApi {
    /**
     * 注册用户
     * @param registerUserRequest
     * @return
     */
    @POST("/user/register")
    Call<ResponseBody> registerUser(@Body RegisterUserRequest registerUserRequest);

    /**
     * 用户登录
     * @param loginUserRequest
     * @return
     */
    @POST("/user/login")
    Call<ResponseBody> loginUser(@Body LoginUserRequest loginUserRequest);

}
