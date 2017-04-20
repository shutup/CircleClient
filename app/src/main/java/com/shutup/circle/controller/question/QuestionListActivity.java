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
import android.widget.Toast;

import com.google.gson.Gson;
import com.shutup.circle.BuildConfig;
import com.shutup.circle.R;
import com.shutup.circle.common.EndlessRecyclerViewScrollListener;
import com.shutup.circle.common.RecyclerTouchListener;
import com.shutup.circle.controller.BaseActivity;
import com.shutup.circle.controller.question.adapter.QuestionListAdapter;
import com.shutup.circle.model.persis.Question;
import com.shutup.circle.model.response.LoginUserResponse;
import com.shutup.circle.model.response.QuestionListResponse;
import com.shutup.circle.model.response.RestInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private EndlessRecyclerViewScrollListener mEndlessRecyclerViewScrollListener;
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
        loadServerData(0);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mEndlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadServerData(page);
            }
        };
        mRecyclerView.addOnScrollListener(mEndlessRecyclerViewScrollListener);
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
        final RealmResults<Question> questions = mRealm.where(Question.class).findAllSortedAsync("createdAt", Sort.DESCENDING);
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

    private void loadServerData(int page) {
        LoginUserResponse loginUserResponse = mRealm.where(LoginUserResponse.class).findFirst();

        Call<ResponseBody> call = getCircleApi().questionTotalList(page,loginUserResponse.getToken());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Gson gson = getGson();
                if (response.isSuccessful()) {
                    try {
                        QuestionListResponse questionListResponse = gson.fromJson(response.body().string(),QuestionListResponse.class);
                        if (questionListResponse.isLast()) {
                            Toast.makeText(QuestionListActivity.this, "已经到底啦！", Toast.LENGTH_SHORT).show();
                        }
                        if (questionListResponse.isFirst()) {
                            mQuestions.clear();
                            mQuestions.addAll(questionListResponse.getContent());
                        }else {
                            mQuestions.addAll(questionListResponse.getContent());
                        }
                        mQuestionListAdapter.notifyDataSetChanged();
                        mSwipeRefresh.setRefreshing(false);

                        mRealm.beginTransaction();
                        mRealm.insertOrUpdate(questionListResponse.getContent());
                        mRealm.commitTransaction();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    RestInfo info = null;
                    try {
                        info = gson.fromJson(response.errorBody().string(), RestInfo.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (info != null) {
                        Toast.makeText(QuestionListActivity.this, info.getMsg(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(QuestionListActivity.this, "请求异常", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void initSwipeRefreshEvent() {
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                mQuestions.clear();
//                mQuestionListAdapter.setQuestions(mQuestions);
//                mQuestionListAdapter.notifyDataSetChanged();
                mEndlessRecyclerViewScrollListener.resetState();
                loadServerData(0);
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
