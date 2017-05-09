package com.shutup.circle.common;

import com.shutup.circle.model.request.LoginUserRequest;
import com.shutup.circle.model.request.QuestionAnswerCommentCreateRequest;
import com.shutup.circle.model.request.QuestionAnswerCreateRequest;
import com.shutup.circle.model.request.QuestionCreateRequest;
import com.shutup.circle.model.request.RegisterUserRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by shutup on 2017/4/4.
 */

public interface CircleApi {
    /**
     * 注册用户
     *
     * @param registerUserRequest
     * @return
     */
    @POST("/user/register")
    Call<ResponseBody> registerUser(@Body RegisterUserRequest registerUserRequest);

    /**
     * 用户登录
     *
     * @param loginUserRequest
     * @return
     */
    @POST("/user/login")
    Call<ResponseBody> loginUser(@Body LoginUserRequest loginUserRequest);

    /**
     * 创建问题
     *
     * @param questionCreateRequest
     * @param token
     * @return
     */
    @POST("/question/create")
    Call<ResponseBody> questionCreate(@Body QuestionCreateRequest questionCreateRequest,
                                      @Header("token") String token);

    /**
     * 创建回答
     *
     * @param questionId
     * @param token
     * @param questionAnswerCreateRequest
     * @return
     */
    @POST("/question/{questionId}/answer/create")
    Call<ResponseBody> questionAnswerAdd(@Path("questionId") Long questionId,
                                         @Header("token") String token,
                                         @Body QuestionAnswerCreateRequest questionAnswerCreateRequest);

    /**
     * 创建回答的评论
     *
     * @param questionId
     * @param answerId
     * @param token
     * @param questionAnswerCommentCreateRequest
     * @return
     */
    @POST("/question/{questionId}/answer/{answerId}/comment/")
    Call<ResponseBody> questionAnswerCommentAdd(@Path("questionId") Long questionId,
                                                @Path("answerId") Long answerId,
                                                @Header("token") String token,
                                                @Body QuestionAnswerCommentCreateRequest questionAnswerCommentCreateRequest
    );

    /**
     * 创建评论回复
     *
     * @param questionId
     * @param answerId
     * @param commentId
     * @param token
     * @param questionAnswerCommentCreateRequest
     * @return
     */
    @POST("/question/{questionId}/answer/{answerId}/comment/{commentId}/reply")
    Call<ResponseBody> questionAnswerCommentAddReply(@Path("questionId") Long questionId,
                                                     @Path("answerId") Long answerId,
                                                     @Path("commentId") Long commentId,
                                                     @Header("token") String token,
                                                     @Body QuestionAnswerCommentCreateRequest questionAnswerCommentCreateRequest
    );

    /**
     * 总的提问列表
     *
     * @param page
     * @param token
     * @return
     */
    @GET("/question/lists")
    Call<ResponseBody> questionTotalList(@Query("page") int page, @Header("token") String token);

    /**
     * 总的提问列表
     * 按点赞、踩排列
     *
     * @param page
     * @param token
     * @return
     */
    @GET("/question/listsBy")
    Call<ResponseBody> questionTotalListByUsersCount(@Query("page") int page, @Header("token") String token,@Query("isAgree") boolean isAgree);

    /**
     * 点赞问题
     *
     * @param questionId
     * @param token
     * @return
     */
    @GET("/question/{questionId}/agree")
    Call<ResponseBody> questionAgree(@Path("questionId") Long questionId, @Header("token") String token);

    /**
     * 踩 问题
     *
     * @param questionId
     * @param token
     * @return
     */
    @GET("/question/{questionId}/disagree")
    Call<ResponseBody> questionDisagree(@Path("questionId") Long questionId, @Header("token") String token);

    /**
     * 点赞回答
     *
     * @param questionId
     * @param answerId
     * @param token
     * @return
     */
    @GET("/question/{questionId}/answer/{answerId}/agree")
    Call<ResponseBody> answerAgree(@Path("questionId") Long questionId,
                                   @Path("answerId") Long answerId,
                                   @Header("token") String token);

    /**
     * 踩 回答
     *
     * @param questionId
     * @param answerId
     * @param token
     * @return
     */
    @GET("/question/{questionId}/answer/{answerId}/disagree")
    Call<ResponseBody> answerDisagree(@Path("questionId") Long questionId,
                                      @Path("answerId") Long answerId,
                                      @Header("token") String token);


    /**
     * 点赞 评论
     *
     * @param questionId
     * @param answerId
     * @param commentId
     * @param token
     * @return
     */
    @GET("/question/{questionId}/answer/{answerId}/comment/{commentId}/agree")
    Call<ResponseBody> commentAgree(@Path("questionId") Long questionId,
                                    @Path("answerId") Long answerId,
                                    @Path("commentId") Long commentId,
                                    @Header("token") String token);

    /**
     * 踩 评论
     *
     * @param questionId
     * @param answerId
     * @param commentId
     * @param token
     * @return
     */
    @GET("/question/{questionId}/answer/{answerId}/comment/{commentId}/disagree")
    Call<ResponseBody> commentDisagree(@Path("questionId") Long questionId,
                                    @Path("answerId") Long answerId,
                                    @Path("commentId") Long commentId,
                                    @Header("token") String token);
}
