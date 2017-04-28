package com.shutup.circle.controller;


import android.support.v4.app.Fragment;

import com.google.gson.Gson;
import com.shutup.circle.common.CircleApi;
import com.shutup.circle.common.GsonSingleton;
import com.shutup.circle.common.RetrofitSingleton;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {

    private CircleApi mCircleApi;
    private Gson mGson;

    public BaseFragment() {
        // Required empty public constructor
        initApiInstance();
        initGsonInstance();
    }

    private void initApiInstance() {
        mCircleApi = RetrofitSingleton.getApiInstance(CircleApi.class);
    }

    public CircleApi getCircleApi() {
        return mCircleApi;
    }

    private void initGsonInstance() {
        mGson = GsonSingleton.getGson();
    }

    public Gson getGson() {
        return mGson;
    }

}
