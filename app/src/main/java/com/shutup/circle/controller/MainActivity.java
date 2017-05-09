package com.shutup.circle.controller;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shutup.circle.R;
import com.shutup.circle.common.Constants;
import com.shutup.circle.controller.me.MeFragment;
import com.shutup.circle.controller.question.QuestionListFragment;
import com.shutup.circle.controller.question.QuestionMainFragment;
import com.shutup.circle.controller.setting.SettingActivity;
import com.shutup.circle.controller.vote.VoteListFragment;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements Constants {

    @InjectView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.layout_vote)
    LinearLayout mLayoutVote;
    @InjectView(R.id.layout_moment)
    LinearLayout mLayoutMoment;
    @InjectView(R.id.layout_me)
    LinearLayout mLayoutMe;
    @InjectView(R.id.layout_question)
    LinearLayout mLayoutQuestion;
    @InjectView(R.id.image_question)
    ImageView mImageQuestion;
    @InjectView(R.id.text_question)
    TextView mTextQuestion;
    @InjectView(R.id.image_vote)
    ImageView mImageVote;
    @InjectView(R.id.text_vote)
    TextView mTextVote;
    @InjectView(R.id.image_moment)
    ImageView mImageMoment;
    @InjectView(R.id.text_moment)
    TextView mTextMoment;
    @InjectView(R.id.image_me)
    ImageView mImageMe;
    @InjectView(R.id.text_me)
    TextView mTextMe;

    private Animation mAnimation;
    private FragmentManager mFragmentManager;
    private ArrayList<Fragment> mFragments;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        initToolBar();
        restoreState();
        initAnimation();
        initFragments();
        selectSection(SECTION_1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_me,menu);
        if (index == SECTION_4) {
            menu.setGroupVisible(R.id.menu_setting_group,true);
        }else {
            menu.setGroupVisible(R.id.menu_setting_group,false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_setting) {
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolBar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }
    }

    private void initFragments() {
        mFragments = new ArrayList<>();
        mFragments.add(new QuestionMainFragment());
        mFragments.add(new VoteListFragment());
        mFragments.add(new VoteListFragment());
        mFragments.add(new MeFragment());
        mFragmentManager = getSupportFragmentManager();
    }

    private void initAnimation() {
        mAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.section_image_scale);
        mImageQuestion.setAnimation(mAnimation);
        mImageVote.setAnimation(mAnimation);
        mImageMoment.setAnimation(mAnimation);
        mImageMe.setAnimation(mAnimation);
    }

    @OnClick({R.id.layout_vote, R.id.layout_moment, R.id.layout_me, R.id.layout_question})
    public void onClick(View view) {
        restoreState();
        switch (view.getId()) {
            case R.id.layout_vote:
                selectSection(SECTION_2);
                break;
            case R.id.layout_moment:
                selectSection(SECTION_3);
                break;
            case R.id.layout_me:
                selectSection(SECTION_4);
                break;
            case R.id.layout_question:
                selectSection(SECTION_1);
                break;
        }
    }

    private void restoreState() {
        mTextQuestion.setTextColor(getResources().getColor(R.color.colorSectionNormal));
        mTextVote.setTextColor(getResources().getColor(R.color.colorSectionNormal));
        mTextMoment.setTextColor(getResources().getColor(R.color.colorSectionNormal));
        mTextMe.setTextColor(getResources().getColor(R.color.colorSectionNormal));
        mImageQuestion.setImageResource(R.drawable.ic_answer);
        mImageVote.setImageResource(R.drawable.ic_vote);
        mImageMoment.setImageResource(R.drawable.ic_moment);
        mImageMe.setImageResource(R.drawable.ic_me);
    }

    private void selectSection(int index) {
        selectTitle(index);
        if (index == SECTION_1) {
            mTextQuestion.setTextColor(getResources().getColor(R.color.colorSectionSelected));
            mImageQuestion.getDrawable().mutate().setColorFilter(getResources().getColor(R.color.colorSectionSelected), PorterDuff.Mode.SRC_IN);
            mImageQuestion.startAnimation(mAnimation);

        } else if (index == SECTION_2) {
            mTextVote.setTextColor(getResources().getColor(R.color.colorSectionSelected));
            mImageVote.getDrawable().mutate().setColorFilter(getResources().getColor(R.color.colorSectionSelected), PorterDuff.Mode.SRC_IN);
            mImageVote.startAnimation(mAnimation);

        } else if (index == SECTION_3) {
            mTextMoment.setTextColor(getResources().getColor(R.color.colorSectionSelected));
            mImageMoment.getDrawable().mutate().setColorFilter(getResources().getColor(R.color.colorSectionSelected), PorterDuff.Mode.SRC_IN);
            mImageMoment.startAnimation(mAnimation);

        } else if (index == SECTION_4) {
            mTextMe.setTextColor(getResources().getColor(R.color.colorSectionSelected));
            mImageMe.getDrawable().mutate().setColorFilter(getResources().getColor(R.color.colorSectionSelected), PorterDuff.Mode.SRC_IN);
            mImageMe.startAnimation(mAnimation);

        }
        selectFragment(index);
    }

    private void selectFragment(int index) {
        mFragmentManager.beginTransaction().replace(R.id.frameLayout, mFragments.get(index - 1)).commit();
    }

    private void selectTitle(int index) {
        this.index = index;
        invalidateOptionsMenu();
        if (index == SECTION_1) {
            mToolbarTitle.setText(R.string.questionAnswerTitle);
        }else if (index == SECTION_2) {
            mToolbarTitle.setText(R.string.voteTitleStr);
        }else if (index == SECTION_3) {
            mToolbarTitle.setText(R.string.commentTitleStr);
        }else if (index == SECTION_4) {
            mToolbarTitle.setText(R.string.meTitleStr);
        }
    }
}
