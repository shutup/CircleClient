package com.shutup.circle.controller.question;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shutup.circle.R;
import com.shutup.circle.controller.BaseActivity;
import com.shutup.circle.model.persis.Question;
import com.shutup.circle.model.request.QuestionAnswerCommentCreateRequest;
import com.shutup.circle.model.response.LoginUserResponse;
import com.shutup.circle.model.response.RestInfo;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.realm.Realm;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentAddActivity extends BaseActivity {

    @InjectView(R.id.commentContent)
    EditText mCommentContent;
    @InjectView(R.id.publishBtn)
    Button mPublishBtn;

    private int questionId;
    private int answerId;
    private int commentId;
    private Long userId ;
    private String hintStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_add);
        ButterKnife.inject(this);
        initParams();
        initCommentContent();
    }

    private void initParams() {
        Intent intent = getIntent();
        questionId = intent.getIntExtra(QUESTION_ID, -1);
        answerId = intent.getIntExtra(ANSWER_ID, -1);
        commentId = intent.getIntExtra(COMMENT_ID,-1);
        userId = intent.getLongExtra(USER_ID,-1);
        hintStr = intent.getStringExtra(HINT_STR);
    }

    private void initCommentContent() {

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
                           public void run() {
                               InputMethodManager inputManager =
                                       (InputMethodManager) mCommentContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                               inputManager.showSoftInput(mCommentContent, 0);
                           }
                       },
                500);
        mCommentContent.setHint(hintStr);
        mCommentContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPublishBtn.setEnabled(editable.toString().trim().length() > 0);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return super.onTouchEvent(event);
    }

    @OnClick(R.id.publishBtn)
    public void onClick() {
        Realm realm = Realm.getDefaultInstance();
        LoginUserResponse loginUserResponse = realm.where(LoginUserResponse.class).findFirst();

        if (isCommentReply() && checkContentNotEmpty() && checkParamsNotEmpty()) {
            if (userId != -1 && userId == loginUserResponse.getId()) {
                processComment(questionId,answerId,loginUserResponse.getToken(),new QuestionAnswerCommentCreateRequest(mCommentContent.getText().toString().trim()));
            }else {
                processCommentReply(questionId,answerId,commentId,loginUserResponse.getToken(),new QuestionAnswerCommentCreateRequest(mCommentContent.getText().toString().trim()));
            }
        }else if (checkContentNotEmpty() && checkParamsNotEmpty()) {
            processComment(questionId,answerId,loginUserResponse.getToken(),new QuestionAnswerCommentCreateRequest(mCommentContent.getText().toString().trim()));

        }
    }

    private void processCommentReply(int questionId,
                                     int answerId,
                                     int commentId,
                                     String token,
                                     QuestionAnswerCommentCreateRequest commentCreateRequest) {
        Call<ResponseBody> call = getCircleApi().questionAnswerCommentAddReply(questionId,
                answerId,commentId,token,commentCreateRequest);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Gson gson = getGson();
                if (response.isSuccessful()) {
                    try {
                        Question question = gson.fromJson(response.body().string(), Question.class);
                        Realm realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        realm.insertOrUpdate(question);
                        realm.commitTransaction();

                        EventBus.getDefault().postSticky(question);
                        finish();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    RestInfo info = null;
                    try {
                        info = gson.fromJson(response.errorBody().string(), RestInfo.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (info != null) {
                        Toast.makeText(CommentAddActivity.this, info.getMsg(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CommentAddActivity.this, "请求异常", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(CommentAddActivity.this, "请求异常", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processComment(int questionId,
                                int answerId,
                                String token,
                                QuestionAnswerCommentCreateRequest commentCreateRequest) {
        Call<ResponseBody> call = getCircleApi().questionAnswerCommentAdd(questionId,
                answerId, token
                , commentCreateRequest);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Gson gson = getGson();
                if (response.isSuccessful()) {
                    try {
                        Question question = gson.fromJson(response.body().string(), Question.class);
                        Realm realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        realm.insertOrUpdate(question);
                        realm.commitTransaction();

                        EventBus.getDefault().postSticky(question);
                        finish();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    RestInfo info = null;
                    try {
                        info = gson.fromJson(response.errorBody().string(), RestInfo.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (info != null) {
                        Toast.makeText(CommentAddActivity.this, info.getMsg(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CommentAddActivity.this, "请求异常", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(CommentAddActivity.this, "请求异常", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean checkContentNotEmpty() {
        return !mCommentContent.getEditableText().toString().trim().equals("");
    }

    private boolean checkParamsNotEmpty() {
        return questionId != -1 && answerId != -1;
    }

    private boolean isCommentReply() {
        return commentId != -1;
    }
}
