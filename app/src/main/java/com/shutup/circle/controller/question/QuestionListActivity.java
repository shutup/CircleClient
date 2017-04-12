package com.shutup.circle.controller.question;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.shutup.circle.BuildConfig;
import com.shutup.circle.controller.question.adapter.QuestionListAdapter;
import com.shutup.circle.R;
import com.shutup.circle.common.RecyclerTouchListener;
import com.shutup.circle.controller.BaseActivity;
import com.shutup.circle.model.persis.Question;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;

public class QuestionListActivity extends BaseActivity {

    @InjectView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @InjectView(R.id.swipeRefresh)
    SwipeRefreshLayout mSwipeRefresh;
    @InjectView(R.id.addQuestionFAB)
    FloatingActionButton mAddQuestionFAB;

    private QuestionListAdapter mQuestionListAdapter;
    private RealmList<Question> mQuestions;
    private Realm mRealm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);
        ButterKnife.inject(this);

        initRealm();
        initToolBar();
        initRecyclerView();
        initSwipeRefreshEvent();
        loadLocalData();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        if (BuildConfig.DEBUG) Log.d("QuestionListActivity", "onStart");
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
        if (BuildConfig.DEBUG) Log.d("QuestionListActivity", "onStop");
    }

    @OnClick(R.id.addQuestionFAB)
    public void onClick() {
        Intent intent = new Intent(QuestionListActivity.this, QuestionAddActivity.class);
        startActivity(intent);
    }

    private void initRealm() {
        mRealm = Realm.getDefaultInstance();
    }

    private void initToolBar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            mToolbarTitle.setText(R.string.questionAnswerTitle);
        }
    }

    private void initRecyclerView() {
        mQuestions = new RealmList<>();
        mQuestionListAdapter = new QuestionListAdapter(this, mQuestions);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mQuestionListAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                    EventBus.getDefault().postSticky(mQuestions.get(position));
                    Intent intent = new Intent(QuestionListActivity.this, QuestionDetailActivity.class);
                    startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void loadLocalData() {
        final RealmResults<Question> questions = mRealm.where(Question.class).findAllSortedAsync("createdAt");
        questions.addChangeListener(new RealmChangeListener<RealmResults<Question>>() {
            @Override
            public void onChange(RealmResults<Question> elements) {
                if (BuildConfig.DEBUG) Log.d("QuestionListActivity", "clear");
                mQuestions.clear();
                mQuestions.addAll(elements);
                mQuestionListAdapter.setQuestions(mQuestions);
                mQuestionListAdapter.notifyDataSetChanged();
                mSwipeRefresh.setRefreshing(false);
            }
        });
    }

    private void initSwipeRefreshEvent() {
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadLocalData();
            }
        });
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onActionRecv(Message msg) {
        if (msg.what==1) {
            loadLocalData();
        }
    }
}
