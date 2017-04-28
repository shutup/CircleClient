package com.shutup.circle.controller.question.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shutup.circle.R;
import com.shutup.circle.common.CircleApi;
import com.shutup.circle.common.Constants;
import com.shutup.circle.common.DateUtils;
import com.shutup.circle.common.GsonSingleton;
import com.shutup.circle.common.RetrofitSingleton;
import com.shutup.circle.common.StringUtils;
import com.shutup.circle.controller.question.CommentAddActivity;
import com.shutup.circle.model.persis.Answer;
import com.shutup.circle.model.persis.Question;
import com.shutup.circle.model.response.LoginUserResponse;
import com.shutup.circle.model.response.RestInfo;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by shutup on 2017/4/9.
 */

public class QuestionAnswerListAdapter extends RecyclerView.Adapter<QuestionAnswerListAdapter.MyViewHolder> implements Constants {
    private Context mContext;
    private Question mQuestion;
    private Gson mGson;
    private CircleApi mCircleApi;

    public QuestionAnswerListAdapter(Context context, Question question) {
        mContext = context;
        mQuestion = question;
        mCircleApi = RetrofitSingleton.getApiInstance(CircleApi.class);
        mGson = GsonSingleton.getGson();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.question_answer_item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Answer answer = mQuestion.getAnswers().get(position);
        if (!answer.isValid()) {
            return;
        }

        holder.mUserPhoto.setBackgroundResource(R.drawable.round_btn_bg);
        holder.mUserPhoto.setText(answer.getUser().getUsername().toUpperCase().substring(0, 1));
        GradientDrawable gradientDrawable = (GradientDrawable) holder.mUserPhoto.getBackground();
        gradientDrawable.setColor(StringUtils.generateColorFromString(answer.getUser().getUsername()));
        holder.mUserName.setText(answer.getUser().getUsername());
        holder.mCreateDateContent.setText(DateUtils.formatMeaningfulDate(mContext.getResources(),answer.getCreatedAt()));
        holder.mAnswerContent.setText(answer.getAnswer());
        holder.mAgreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processAgreeOrDisagree(holder,true);
            }
        });
        holder.mDisagreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processAgreeOrDisagree(holder,false);
            }
        });
        holder.mAgreeNum.setText(answer.getAgreedUsers().size() + "");
        holder.mDisagreeNum.setText(answer.getDisagreedUsers().size() + "");

        holder.mContentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CommentAddActivity.class);
                intent.putExtra(QUESTION_ID, mQuestion.getId());
                intent.putExtra(ANSWER_ID, answer.getId());
                intent.putExtra(HINT_STR, "回复：" + answer.getUser().getUsername());
                mContext.startActivity(intent);
            }
        });
        CommentListAdapter commentListAdapter = new CommentListAdapter(mContext, mQuestion, answer, position);
        holder.mCommentRecyclerView.setAdapter(commentListAdapter);
        holder.mCommentRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @Override
    public int getItemCount() {
        return mQuestion.getAnswers().size();
    }

    public void setQuestion(Question question) {
        mQuestion = question;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.userPhoto)
        Button mUserPhoto;
        @InjectView(R.id.userName)
        TextView mUserName;
        @InjectView(R.id.createDateContent)
        TextView mCreateDateContent;
        @InjectView(R.id.answerContent)
        TextView mAnswerContent;
        @InjectView(R.id.agreeBtn)
        ImageButton mAgreeBtn;
        @InjectView(R.id.disagreeBtn)
        ImageButton mDisagreeBtn;
        @InjectView(R.id.agreeNum)
        TextView mAgreeNum;
        @InjectView(R.id.disagreeNum)
        TextView mDisagreeNum;
        @InjectView(R.id.commentRecyclerView)
        RecyclerView mCommentRecyclerView;

        @InjectView(R.id.contentLayout)
        RelativeLayout mContentLayout;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

    private void processAgreeOrDisagree(final MyViewHolder holder, boolean isAgree){
        Realm realm = Realm.getDefaultInstance();
        LoginUserResponse loginUserResponse = realm.where(LoginUserResponse.class).findFirst();
        Answer answer = mQuestion.getAnswers().get(holder.getAdapterPosition());
        Call<ResponseBody> call = null;
        if (isAgree) {
            call = mCircleApi.answerAgree(mQuestion.getId(),answer.getId(),loginUserResponse.getToken());
        }else {
            call = mCircleApi.answerDisagree(mQuestion.getId(),answer.getId(),loginUserResponse.getToken());
        }

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Gson gson = mGson;
                if (response.isSuccessful()) {
                    try {
                        Question question = gson.fromJson(response.body().string(),Question.class);
                        Realm realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        realm.insertOrUpdate(question);
                        realm.commitTransaction();
                        mQuestion = question;
                        notifyItemChanged(holder.getAdapterPosition());
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
                        Toast.makeText(mContext, info.getMsg(), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(mContext, "请求异常", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(mContext, "请求异常", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
