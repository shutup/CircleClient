package com.shutup.circle.common;

import com.shutup.circle.model.request.LoginUserRequest;
import com.shutup.circle.model.request.QuestionAnswerCommentCreateRequest;
import com.shutup.circle.model.request.QuestionAnswerCreateRequest;
import com.shutup.circle.model.request.QuestionCreateRequest;
import com.shutup.circle.model.request.RegisterUserRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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
    @POST("/v1/users")
    Call<ResponseBody> registerUser(@Body RegisterUserRequest registerUserRequest);

    /**
     * 用户登录
     *
     * @param loginUserRequest
     * @return
     */
    @POST("/v1/users/login")
    Call<ResponseBody> loginUser(@Body LoginUserRequest loginUserRequest);

    /**
     * 创建问题
     *
     * @param questionCreateRequest
     * @param token
     * @return
     */
    @POST("/v1/questions")
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
    @POST("/v1/questions/{questionId}/answers")
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
    @POST("/v1/questions/{questionId}/answers/{answerId}/comments/")
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
    @POST("/v1/questions/{questionId}/answers/{answerId}/comments/{commentId}/replies")
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
    @GET("/v1/questions/")
    Call<ResponseBody> questionTotalList(@Query("page") int page, @Header("token") String token);

    /**
     * 总的提问列表
     *
     * @param page
     * @param token
     * @return
     */
    @GET("/v1/questions/")
    Call<ResponseBody> questionTotalList(@Query("page") int page,
                                         @Query("sort") String sort,
                                         @Header("token") String token);

    /**
     * 总的提问列表
     *
     * @param page
     * @param token
     * @return
     */
    @GET("/v1/questions/")
    Call<ResponseBody> questionTotalList(@Query("page") int page,
                                         @Query("limit") int limit,
                                         @Query("sort") String sort,
                                         @Header("token") String token);

    /**
     * 点赞问题
     *
     * @param questionId
     * @param token
     * @return
     */
    @PUT("/v1/questions/{questionId}/agree")
    Call<ResponseBody> questionAgree(@Path("questionId") Long questionId, @Header("token") String token);

    /**
     * 踩 问题
     *
     * @param questionId
     * @param token
     * @return
     */
    @DELETE("/v1/questions/{questionId}/agree")
    Call<ResponseBody> questionDisagree(@Path("questionId") Long questionId, @Header("token") String token);

    /**
     * 点赞回答
     *
     * @param questionId
     * @param answerId
     * @param token
     * @return
     */
    @PUT("/v1/questions/{questionId}/answers/{answerId}/agree")
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
    @DELETE("/v1/questions/{questionId}/answers/{answerId}/agree")
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
    @PUT("/v1/questions/{questionId}/answers/{answerId}/comments/{commentId}/agree")
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
    @DELETE("/v1/questions/{questionId}/answers/{answerId}/comments/{commentId}/agree")
    Call<ResponseBody> commentDisagree(@Path("questionId") Long questionId,
                                    @Path("answerId") Long answerId,
                                    @Path("commentId") Long commentId,
                                    @Header("token") String token);
}
