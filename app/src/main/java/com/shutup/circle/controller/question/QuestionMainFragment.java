package com.shutup.circle.controller.question;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shutup.circle.R;
import com.shutup.circle.common.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class QuestionMainFragment extends Fragment implements Constants{

    @InjectView(R.id.viewPager)
    ViewPager mViewPager;
    private List<String> mTabTitles;
    private List<Fragment> mFragmentList;

    public QuestionMainFragment() {
        // Required empty public constructor
        mTabTitles = new ArrayList<>();
        mTabTitles.add("最新");
        mTabTitles.add("最多赞");
        mTabTitles.add("最多踩");
        mFragmentList = new ArrayList<>();
        QuestionListFragment questionListFragmentByTime = new QuestionListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(QUESTION_ORDER_BY_TYPE,QUESTION_ORDER_BY_TIME);
        questionListFragmentByTime.setArguments(bundle);
        QuestionListFragment questionListFragmentByAgree = new QuestionListFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putInt(QUESTION_ORDER_BY_TYPE,QUESTION_ORDER_BY_AGREEDUSERS);
        questionListFragmentByAgree.setArguments(bundle1);
        QuestionListFragment questionListFragmentByDisagree = new QuestionListFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putInt(QUESTION_ORDER_BY_TYPE,QUESTION_ORDER_BY_DISAGREEDUSERS);
        questionListFragmentByDisagree.setArguments(bundle2);
        mFragmentList.add(questionListFragmentByTime);
        mFragmentList.add(questionListFragmentByAgree);
        mFragmentList.add(questionListFragmentByDisagree);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_question_main, container, false);
        ButterKnife.inject(this, view);
        mViewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTabTitles.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
