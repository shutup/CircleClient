package com.shutup.circle.common;

import com.shutup.circle.model.request.LoginUserRequest;
import com.shutup.circle.model.request.QuestionAnswerCommentCreateRequest;
import com.shutup.circle.model.request.QuestionAnswerCreateRequest;
import com.shutup.circle.model.request.QuestionCreateRequest;
import com.shutup.circle.model.request.RegisterUserRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

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

    /**
     *
     * @param questionCreateRequest
     * @param token
     * @return
     */
    @POST("/question/create")
    Call<ResponseBody> questionCreate(@Body QuestionCreateRequest questionCreateRequest,
                                      @Header("token") String token);


    @POST("/question/{questionId}/answer/create")
    Call<ResponseBody> questionAnswerAdd(@Path("questionId") int questionId,
                                         @Header("token") String token,
                                         @Body QuestionAnswerCreateRequest questionAnswerCreateRequest);

    @POST("/question/{questionId}/answer/{answerId}/comment/")
    Call<ResponseBody> questionAnswerCommentAdd(@Path("questionId") int questionId,
                                                @Path("answerId") int answerId,
                                                @Header("token") String token,
                                                @Body QuestionAnswerCommentCreateRequest questionAnswerCommentCreateRequest
                                                );

    @POST("/question/{questionId}/answer/{answerId}/comment/{commentId}/reply")
    Call<ResponseBody> questionAnswerCommentAddReply(@Path("questionId") int questionId,
                                                @Path("answerId") int answerId,
                                                @Path("commentId") int commentId,
                                                @Header("token") String token,
                                                @Body QuestionAnswerCommentCreateRequest questionAnswerCommentCreateRequest
    );
}
