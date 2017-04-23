package com.shutup.circle.controller.question.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shutup.circle.BuildConfig;
import com.shutup.circle.R;
import com.shutup.circle.common.CircleApi;
import com.shutup.circle.common.Constants;
import com.shutup.circle.common.DateUtils;
import com.shutup.circle.common.GsonSingleton;
import com.shutup.circle.common.RetrofitSingleton;
import com.shutup.circle.controller.question.CommentAddActivity;
import com.shutup.circle.model.persis.Answer;
import com.shutup.circle.model.persis.Comment;
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
 * Created by shutup on 2017/4/11.
 */

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.MyViewHolder> implements Constants {
    private final CircleApi mCircleApi;
    private final Gson mGson;
    private Context mContext;
    private Question mQuestion;
    private Answer mAnswer;
    private int mIndex;

    public CommentListAdapter(Context context, Question question, Answer answer,int index) {
        mContext = context;
        mQuestion = question;
        mAnswer = answer;
        mIndex = index;
        mCircleApi = RetrofitSingleton.getApiInstance(CircleApi.class);
        mGson = GsonSingleton.getGson();
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

        holder.mAgreeNum.setText(comment.getAgreedUsers().size() + "");
        holder.mDisagreeNum.setText(comment.getDisagreedUsers().size() + "");
        holder.mCreateDateContent.setText(DateUtils.formatMeanningfulDate(comment.getCreatedAt()));
        if (comment.isReply()) {
            holder.mUserPhoto.setBackgroundResource(R.drawable.round_btn_bg);
            holder.mUserPhoto.setText(comment.getReplyUser().getUsername().toUpperCase().substring(0, 1));
            int color = Color.HSVToColor(new float[]{(float) Math.random(), (float) Math.random(), 0.5F + ((float) Math.random()) / 2F});
            GradientDrawable gradientDrawable = (GradientDrawable) holder.mUserPhoto.getBackground();
            gradientDrawable.setColor(color);
            holder.mUserName.setText(comment.getReplyUser().getUsername());
            final String userName = comment.getUser().getUsername();
            SpannableString usernameStr = new SpannableString(userName);
            usernameStr.setSpan(new ForegroundColorSpan(Color.GREEN), 0, usernameStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            usernameStr.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    if (BuildConfig.DEBUG) Log.d("CommentListAdapter", "view:" + view);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setUnderlineText(false);
                }
            }, 0, usernameStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            SpannableString commentStr = new SpannableString(comment.getComment());
            commentStr.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, CommentAddActivity.class);
                    intent.putExtra(QUESTION_ID, mQuestion.getId());
                    intent.putExtra(ANSWER_ID, mAnswer.getId());
                    intent.putExtra(COMMENT_ID, mAnswer.getComments().get(holder.getAdapterPosition()).getId());
                    intent.putExtra(USER_ID, mAnswer.getComments().get(holder.getAdapterPosition()).getUser().getId());
                    intent.putExtra(HINT_STR, "回复：" + mAnswer.getComments().get(holder.getAdapterPosition()).getUser().getUsername());
                    mContext.startActivity(intent);
                }
                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setUnderlineText(false);
                }

            },0,commentStr.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.mContent.setMovementMethod(LinkMovementMethod.getInstance());
            holder.mContent.setText(TextUtils.concat(new SpannableString("回复 "),usernameStr,new SpannableString(" : "), commentStr));
        } else {
            holder.mUserPhoto.setBackgroundResource(R.drawable.round_btn_bg);
            holder.mUserPhoto.setText(comment.getUser().getUsername().toUpperCase().substring(0, 1));
            int color = Color.HSVToColor(new float[]{(float) Math.random(), (float) Math.random(), 0.5F + ((float) Math.random()) / 2F});
            GradientDrawable gradientDrawable = (GradientDrawable) holder.mUserPhoto.getBackground();
            gradientDrawable.setColor(color);
            holder.mUserName.setText(comment.getUser().getUsername());
            holder.mContent.setText(comment.getComment());
        }
//        if (comment.isReply()) {
//            final String userName = comment.getUser().getUsername();
//            SpannableString usernameStr = new SpannableString(userName);
//            usernameStr.setSpan(new ForegroundColorSpan(Color.GREEN), 0, usernameStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            usernameStr.setSpan(new ClickableSpan() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(mContext, userName, Toast.LENGTH_SHORT).show();
//                }
//            }, 0, usernameStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            final String replyUserName = comment.getReplyUser().getUsername();
//            SpannableString replyUserNameStr = new SpannableString(replyUserName);
//            replyUserNameStr.setSpan(new ForegroundColorSpan(Color.GREEN), 0, replyUserNameStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            replyUserNameStr.setSpan(new ClickableSpan() {
//                @Override
//                public void onClick(View view) {
//                    if (BuildConfig.DEBUG) Log.d("CommentListAdapter", "clicked");
//                    Toast.makeText(mContext, replyUserName, Toast.LENGTH_SHORT).show();
//                }
//            }, 0, replyUserNameStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            SpannableString commentStr = new SpannableString(comment.getComment());
//            holder.mContent.setText(TextUtils.concat(replyUserNameStr, new SpannableString(" 回复 "), usernameStr, new SpannableString(" : "), commentStr));
//        } else {
//            SpannableString usernameStr = new SpannableString(comment.getUser().getUsername() + ":");
//            usernameStr.setSpan(new ForegroundColorSpan(Color.GREEN), 0, usernameStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            usernameStr.setSpan(new ClickableSpan() {
//                @Override
//                public void onClick(View view) {
//                    if (BuildConfig.DEBUG) Log.d("CommentListAdapter", "clicked");
//                    Toast.makeText(mContext, "clicked", Toast.LENGTH_SHORT).show();
//                }
//            }, 0, usernameStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            SpannableString commentStr = new SpannableString(comment.getComment());
//            holder.mContent.setText(TextUtils.concat(usernameStr, commentStr));
//        }
        holder.mContentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CommentAddActivity.class);
                intent.putExtra(QUESTION_ID, mQuestion.getId());
                intent.putExtra(ANSWER_ID, mAnswer.getId());
                intent.putExtra(COMMENT_ID, mAnswer.getComments().get(holder.getAdapterPosition()).getId());
                intent.putExtra(USER_ID, mAnswer.getComments().get(holder.getAdapterPosition()).getUser().getId());
                intent.putExtra(HINT_STR, "回复：" + mAnswer.getComments().get(holder.getAdapterPosition()).getUser().getUsername());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAnswer.getComments().size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.userPhoto)
        Button mUserPhoto;
        @InjectView(R.id.userName)
        TextView mUserName;
        @InjectView(R.id.createDateContent)
        TextView mCreateDateContent;
        @InjectView(R.id.content)
        TextView mContent;
        @InjectView(R.id.agreeBtn)
        ImageButton mAgreeBtn;
        @InjectView(R.id.agreeNum)
        TextView mAgreeNum;
        @InjectView(R.id.disagreeBtn)
        ImageButton mDisagreeBtn;
        @InjectView(R.id.disagreeNum)
        TextView mDisagreeNum;
        @InjectView(R.id.contentLayout)
        RelativeLayout mContentLayout;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

    private void processAgreeOrDisagree(final CommentListAdapter.MyViewHolder holder, boolean isAgree){
        Realm realm = Realm.getDefaultInstance();
        LoginUserResponse loginUserResponse = realm.where(LoginUserResponse.class).findFirst();
        Comment comment = mAnswer.getComments().get(holder.getAdapterPosition());
        Call<ResponseBody> call = null;
        if (isAgree) {
            call = mCircleApi.commentAgree(mQuestion.getId(),mAnswer.getId(),comment.getId(),loginUserResponse.getToken());
        }else {
            call = mCircleApi.commentDisagree(mQuestion.getId(),mAnswer.getId(),comment.getId(),loginUserResponse.getToken());
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
                        mAnswer = question.getAnswers().get(mIndex);
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
