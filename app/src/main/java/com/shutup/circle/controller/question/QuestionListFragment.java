package com.shutup.circle.controller.question;


import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shutup.circle.BuildConfig;
import com.shutup.circle.R;
import com.shutup.circle.common.Constants;
import com.shutup.circle.common.EndlessRecyclerViewScrollListener;
import com.shutup.circle.common.RecyclerTouchListener;
import com.shutup.circle.controller.BaseFragment;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionListFragment extends BaseFragment implements Constants{


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
    private int currentType = QUESTION_ORDER_BY_TIME;

    public QuestionListFragment() {
        if (BuildConfig.DEBUG) Log.d("QuestionListFragment", "frame");
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) Log.d("QuestionListFragment", "onCreateView");
        Bundle bundle = getArguments();
        currentType = bundle.getInt(QUESTION_ORDER_BY_TYPE,QUESTION_ORDER_BY_TIME);
        if (BuildConfig.DEBUG) Log.d("QuestionListFragment", "currentType:" + currentType);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_question_list, container, false);
        ButterKnife.inject(this, view);
        initRealm();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecyclerView();
        initSwipeRefreshEvent();
        loadLocalData();
        loadServerData(0);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) Log.d("QuestionListFragment", "onCreate");
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        if (BuildConfig.DEBUG) Log.d("QuestionListFragment", "onDestroy");
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @OnClick(R.id.addQuestionFAB)
    public void onClick() {
        Intent intent = new Intent(getActivity(), QuestionAddActivity.class);
        startActivity(intent);
    }

    private void initRealm() {
        mRealm = Realm.getDefaultInstance();
    }

    private void initRecyclerView() {
        mQuestions = new RealmList<>();
        mQuestionListAdapter = new QuestionListAdapter(getActivity(), mQuestions);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mEndlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadServerData(page);
            }
        };
        mRecyclerView.addOnScrollListener(mEndlessRecyclerViewScrollListener);
        mRecyclerView.setAdapter(mQuestionListAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                EventBus.getDefault().postSticky(mQuestions.get(position));
                Intent intent = new Intent(getActivity(), QuestionDetailActivity.class);
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
                if (mSwipeRefresh == null) {
                    return;
                }
                mSwipeRefresh.setRefreshing(false);
            }
        });
    }

    private void loadServerData(int page) {
        LoginUserResponse loginUserResponse = mRealm.where(LoginUserResponse.class).findFirst();

        Call<ResponseBody> call=null;
        if (currentType == QUESTION_ORDER_BY_TIME) {
            call = getCircleApi().questionTotalList(page,loginUserResponse.getToken());
        }else if (currentType == QUESTION_ORDER_BY_AGREEDUSERS) {
            call = getCircleApi().questionTotalList(page,QUESTION_ORDER_BY_AGREEUSERS,loginUserResponse.getToken());
        }else if (currentType == QUESTION_ORDER_BY_DISAGREEDUSERS) {
            call = getCircleApi().questionTotalList(page,QUESTION_ORDER_BY_DISAGREEUSERS,loginUserResponse.getToken());
        }
        if (null == call)
            return;
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Gson gson = getGson();
                if (response.isSuccessful()) {
                    try {
                        QuestionListResponse questionListResponse = gson.fromJson(response.body().string(),QuestionListResponse.class);
                        if (questionListResponse.isLast()) {
                            Toast.makeText(getActivity(), "已经到底啦！", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), info.getMsg(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "请求异常", Toast.LENGTH_SHORT).show();
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
