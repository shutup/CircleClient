package com.shutup.circle.controller.question;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shutup.circle.R;
import com.shutup.circle.controller.BaseActivity;
import com.shutup.circle.model.persis.Question;
import com.shutup.circle.model.request.QuestionAnswerCreateRequest;
import com.shutup.circle.model.response.LoginUserResponse;
import com.shutup.circle.model.response.RestInfo;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.realm.Realm;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionAnswerAddActivity extends BaseActivity {

    @InjectView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.todoContent)
    EditText mTodoContent;
    @InjectView(R.id.addBtn)
    Button mAddBtn;

    private int questionId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_answer_add);
        ButterKnife.inject(this);
        initToolBar();
        initQuestionId();
    }

    private void initQuestionId() {
        Intent intent = getIntent();
        questionId = intent.getIntExtra(QUESTION_ID,-1);
    }

    private void initToolBar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
            mToolbarTitle.setText(R.string.addQuestionTitle);
        }
    }

    private boolean checkContentNotEmpty() {
        return !mTodoContent.getEditableText().toString().trim().equals("");
    }

    private boolean checkQuestionNotNull() {
        return questionId != -1;
    }

    @OnClick(R.id.addBtn)
    public void onClick() {
        if (checkContentNotEmpty() && checkQuestionNotNull()) {
            Realm realm = Realm.getDefaultInstance();
            LoginUserResponse loginUserResponse = realm.where(LoginUserResponse.class).findFirst();
            Call<ResponseBody> call = getCircleApi().questionAnswerAdd(questionId,
                    loginUserResponse.getToken(),
                    new QuestionAnswerCreateRequest(mTodoContent.getEditableText().toString().trim()));

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Gson gson = getGson();
                    if (response.isSuccessful()) {
                        try {
                            Question question = gson.fromJson(response.body().string(),Question.class);
                            Realm realm = Realm.getDefaultInstance();
                            realm.beginTransaction();
                            realm.insertOrUpdate(question);
                            realm.commitTransaction();

                            refreshUI();
                            finish();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        RestInfo info = null;
                        try {
                            info = gson.fromJson(response.errorBody().string(),RestInfo.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (info!= null) {
                            Toast.makeText(QuestionAnswerAddActivity.this, info.getMsg(), Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(QuestionAnswerAddActivity.this, "请求异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(QuestionAnswerAddActivity.this, "请求异常", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
