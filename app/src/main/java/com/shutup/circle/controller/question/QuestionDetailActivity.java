package com.shutup.circle.controller.question;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.shutup.circle.R;
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
    Button mAgreeBtn;
    @InjectView(R.id.disagreeBtn)
    Button mDisagreeBtn;

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
        mQuestionContent.setText(question.getQuestion());
        mAgreeBtn.setText(question.getAgreedUsers().size()+"");
        mDisagreeBtn.setText(question.getDisagreedUsers().size()+"");
        mQuestionAnswerListAdapter.setQuestion(mQuestion);
        mQuestionAnswerListAdapter.notifyDataSetChanged();
    }

    private void initRecyclerView() {
        mQuestionAnswerListAdapter = new QuestionAnswerListAdapter(this, mQuestion);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mQuestionAnswerListAdapter);
//        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//                Intent intent = new Intent(QuestionDetailActivity.this, CommentAddActivity.class);
//                intent.putExtra(QUESTION_ID,mQuestion.getId());
//                intent.putExtra(ANSWER_ID,mAnswers.get(position).getId());
//                startActivity(intent);
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//
//            }
//        }));
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @OnClick({R.id.agreeBtn, R.id.disagreeBtn,R.id.addAnswerFAB})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.agreeBtn:
                break;
            case R.id.disagreeBtn:
                break;
            case R.id.addAnswerFAB:
                Intent intent = new Intent(QuestionDetailActivity.this, QuestionAnswerAddActivity.class);
                intent.putExtra(QUESTION_ID,mQuestion.getId());
                startActivity(intent);
                break;
        }
    }
}
