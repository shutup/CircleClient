package com.shutup.circle.controller.question.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.shutup.circle.BuildConfig;
import com.shutup.circle.R;
import com.shutup.circle.common.Constants;
import com.shutup.circle.controller.question.CommentAddActivity;
import com.shutup.circle.model.persis.Answer;
import com.shutup.circle.model.persis.Comment;
import com.shutup.circle.model.persis.Question;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by shutup on 2017/4/11.
 */

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.MyViewHolder> implements Constants{
    private Context mContext;
    private Question mQuestion;
    private Answer mAnswer;

    public CommentListAdapter(Context context, Question question,Answer answer) {
        mContext = context;
        mQuestion = question;
        mAnswer = answer;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.comment_item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Comment comment = mAnswer.getComments().get(position);
        if (!comment.isValid()) {
            return;
        }
        if (comment.isReply()) {
            final String userName = comment.getUser().getUsername();
            SpannableString usernameStr = new SpannableString(userName);
            usernameStr.setSpan(new ForegroundColorSpan(Color.GREEN),0,usernameStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            usernameStr.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, userName, Toast.LENGTH_SHORT).show();
                }
            },0,usernameStr.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            final String replyUserName = comment.getReplyUser().getUsername();
            SpannableString replyUserNameStr = new SpannableString(replyUserName);
            replyUserNameStr.setSpan(new ForegroundColorSpan(Color.GREEN),0,replyUserNameStr.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            replyUserNameStr.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    if (BuildConfig.DEBUG) Log.d("CommentListAdapter", "clicked");
                    Toast.makeText(mContext, replyUserName, Toast.LENGTH_SHORT).show();
                }
            },0,replyUserNameStr.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            SpannableString commentStr = new SpannableString(comment.getComment());
            holder.mContent.setText(TextUtils.concat(replyUserNameStr,new SpannableString(" 回复 "),usernameStr,new SpannableString(" : "),commentStr));
        }else {
            SpannableString usernameStr = new SpannableString(comment.getUser().getUsername()+":");
            usernameStr.setSpan(new ForegroundColorSpan(Color.GREEN),0,usernameStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            usernameStr.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    if (BuildConfig.DEBUG) Log.d("CommentListAdapter", "clicked");
                    Toast.makeText(mContext, "clicked", Toast.LENGTH_SHORT).show();
                }
            },0,usernameStr.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            SpannableString commentStr = new SpannableString(comment.getComment());
            holder.mContent.setText(TextUtils.concat(usernameStr,commentStr));
        }
        holder.mContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CommentAddActivity.class);
                intent.putExtra(QUESTION_ID, mQuestion.getId());
                intent.putExtra(ANSWER_ID, mAnswer.getId());
                intent.putExtra(COMMENT_ID, mAnswer.getComments().get(holder.getAdapterPosition()).getId());
                intent.putExtra(USER_ID, mAnswer.getComments().get(holder.getAdapterPosition()).getUser().getId());
                intent.putExtra(HINT_STR,"回复："+mAnswer.getComments().get(holder.getAdapterPosition()).getUser().getUsername());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAnswer.getComments().size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        @InjectView(R.id.content)
        TextView mContent;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }
}
