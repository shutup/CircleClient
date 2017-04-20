package com.shutup.circle.controller.question.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.shutup.circle.R;
import com.shutup.circle.common.Constants;
import com.shutup.circle.common.DateUtils;
import com.shutup.circle.controller.question.CommentAddActivity;
import com.shutup.circle.model.persis.Answer;
import com.shutup.circle.model.persis.Question;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by shutup on 2017/4/9.
 */

public class QuestionAnswerListAdapter extends RecyclerView.Adapter<QuestionAnswerListAdapter.MyViewHolder> implements Constants {
    private Context mContext;
    private Question mQuestion;

    public QuestionAnswerListAdapter(Context context, Question question) {
        mContext = context;
        mQuestion = question;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.question_answer_item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Answer answer = mQuestion.getAnswers().get(position);
        if (!answer.isValid()) {
            return;
        }

        holder.mUserPhoto.setBackgroundResource(R.drawable.round_btn_bg);
        holder.mUserPhoto.setText(answer.getUser().getUsername().toUpperCase().substring(0, 1));
        int color = Color.HSVToColor(new float[]{(float) Math.random(), (float) Math.random(), 0.5F + ((float) Math.random()) / 2F});
        GradientDrawable gradientDrawable = (GradientDrawable) holder.mUserPhoto.getBackground();
        gradientDrawable.setColor(color);
        holder.mUserName.setText(answer.getUser().getUsername());
        holder.mCreateDateContent.setText(DateUtils.formatDate(answer.getCreatedAt()));
        holder.mAnswerContent.setText(answer.getAnswer());
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
        CommentListAdapter commentListAdapter = new CommentListAdapter(mContext, mQuestion, answer);
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
}
