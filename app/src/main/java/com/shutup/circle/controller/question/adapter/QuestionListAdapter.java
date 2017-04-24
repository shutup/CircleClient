package com.shutup.circle.controller.question.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shutup.circle.R;
import com.shutup.circle.common.DateUtils;
import com.shutup.circle.model.persis.Question;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.RealmList;

/**
 * Created by shutup on 2017/4/8.
 */

public class QuestionListAdapter extends RecyclerView.Adapter<QuestionListAdapter.MyViewHolder> {
    private Context mContext;
    private RealmList<Question> mQuestions;

    public QuestionListAdapter(Context context, RealmList<Question> questions) {
        mContext = context;
        mQuestions = questions;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Question question = mQuestions.get(position);
        if (!question.isValid()) {
            return;
        }
        holder.mQuestionContent.setText(question.getQuestion());
        holder.mAnswerNumContent.setText(question.getAnswers().size() + mContext.getString(R.string.answerNumTitle));
        holder.mCreateDateContent.setText(DateUtils.formatMeanningfulDate(question.getCreatedAt()));

    }


    @Override
    public int getItemCount() {
        return mQuestions.size();
    }

    public RealmList<Question> getQuestions() {
        return mQuestions;
    }

    public void setQuestions(RealmList<Question> questions) {
        mQuestions = questions;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.questionContent)
        TextView mQuestionContent;
        @InjectView(R.id.answerNumContent)
        TextView mAnswerNumContent;
        @InjectView(R.id.createDateContent)
        TextView mCreateDateContent;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

}


