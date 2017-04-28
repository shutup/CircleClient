package com.shutup.circle.controller.question;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shutup.circle.R;
import com.shutup.circle.common.DateUtils;
import com.shutup.circle.common.StringUtils;
import com.shutup.circle.controller.BaseActivity;
import com.shutup.circle.controller.question.adapter.QuestionAnswerListAdapter;
import com.shutup.circle.model.persis.Question;
import com.shutup.circle.model.response.LoginUserResponse;
import com.shutup.circle.model.response.RestInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.realm.Realm;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionDetailActivity extends BaseActivity {

    @InjectView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @InjectView(R.id.addAnswerFAB)
    FloatingActionButton mAddAnswerFAB;
    @InjectView(R.id.questionContent)
    TextView mQuestionContent;
    @InjectView(R.id.agreeBtn)
    ImageButton mAgreeBtn;
    @InjectView(R.id.disagreeBtn)
    ImageButton mDisagreeBtn;
    @InjectView(R.id.userPhoto)
    Button mUserPhoto;
    @InjectView(R.id.userName)
    TextView mUserName;
    @InjectView(R.id.createDateContent)
    TextView mCreateDateContent;
    @InjectView(R.id.agreeNum)
    TextView mAgreeNum;
    @InjectView(R.id.disagreeNum)
    TextView mDisagreeNum;


    private Question mQuestion;
    private QuestionAnswerListAdapter mQuestionAnswerListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);
        ButterKnife.inject(this);
        initToolBar();
        initRecyclerView();
    }

    @Override
    public void onStart() {
        super.onStart();
        reloadUI(mQuestion);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void initToolBar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
            mToolbarTitle.setText(R.string.questionAnswerTitle);
        }
    }

    @Subscribe(sticky = true)
    public void onQuestionReceive(Question question) {
        mQuestion = question;
        reloadUI(mQuestion);
        EventBus.getDefault().removeStickyEvent(question);
    }

    private void reloadUI(Question question) {
        if (question == null) {
            return;
        }
        mUserPhoto.setBackgroundResource(R.drawable.round_btn_bg);
        mUserPhoto.setText(question.getUser().getUsername().toUpperCase().substring(0, 1));
        GradientDrawable gradientDrawable = (GradientDrawable) mUserPhoto.getBackground();
        gradientDrawable.setColor(StringUtils.generateColorFromString(question.getUser().getUsername()));
        mUserName.setText(question.getUser().getUsername());
        mCreateDateContent.setText(DateUtils.formatMeaningfulDate(getResources(),question.getCreatedAt()));
        mQuestionContent.setText(question.getQuestion());
        mAgreeNum.setText(question.getAgreedUsers().size() + "");
        mDisagreeNum.setText(question.getDisagreedUsers().size() + "");
        mQuestionAnswerListAdapter.setQuestion(mQuestion);
        mQuestionAnswerListAdapter.notifyDataSetChanged();
    }

    private void initRecyclerView() {
        mQuestionAnswerListAdapter = new QuestionAnswerListAdapter(this, mQuestion);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mQuestionAnswerListAdapter);
    }

    @OnClick({R.id.agreeBtn, R.id.disagreeBtn, R.id.addAnswerFAB})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.agreeBtn:
                processAgreeOrDisagree(true);
                break;
            case R.id.disagreeBtn:
                processAgreeOrDisagree(false);
                break;
            case R.id.addAnswerFAB:
                Intent intent = new Intent(QuestionDetailActivity.this, QuestionAnswerAddActivity.class);
                intent.putExtra(QUESTION_ID, mQuestion.getId());
                startActivity(intent);
                break;
        }
    }

    private void processAgreeOrDisagree(boolean isAgree){
        Realm realm = Realm.getDefaultInstance();
        LoginUserResponse loginUserResponse = realm.where(LoginUserResponse.class).findFirst();
        Call<ResponseBody> call = null;
        if (isAgree) {
           call = getCircleApi().questionAgree(mQuestion.getId(),loginUserResponse.getToken());
        }else {
           call = getCircleApi().questionDisagree(mQuestion.getId(),loginUserResponse.getToken());
        }

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

                        reloadUI(question);
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
                        Toast.makeText(QuestionDetailActivity.this, info.getMsg(), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(QuestionDetailActivity.this, "请求异常", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(QuestionDetailActivity.this, "请求异常", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
