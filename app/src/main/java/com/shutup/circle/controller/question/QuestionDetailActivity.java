package com.shutup.circle.controller.question;

import android.content.Intent;
import android.graphics.Color;
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

import com.shutup.circle.R;
import com.shutup.circle.common.DateUtils;
import com.shutup.circle.controller.BaseActivity;
import com.shutup.circle.controller.question.adapter.QuestionAnswerListAdapter;
import com.shutup.circle.model.persis.Question;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

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
        int color = Color.HSVToColor(new float[]{(float) Math.random(), (float) Math.random(), 0.5F + ((float) Math.random()) / 2F});
        GradientDrawable gradientDrawable = (GradientDrawable) mUserPhoto.getBackground();
        gradientDrawable.setColor(color);
        mUserName.setText(question.getUser().getUsername());
        mCreateDateContent.setText(DateUtils.formatDate(question.getCreatedAt()));
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
                break;
            case R.id.disagreeBtn:
                break;
            case R.id.addAnswerFAB:
                Intent intent = new Intent(QuestionDetailActivity.this, QuestionAnswerAddActivity.class);
                intent.putExtra(QUESTION_ID, mQuestion.getId());
                startActivity(intent);
                break;
        }
    }
}
