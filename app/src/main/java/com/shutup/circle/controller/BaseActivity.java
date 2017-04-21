package com.shutup.circle.controller;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;
import com.shutup.circle.BuildConfig;
import com.shutup.circle.common.CircleApi;
import com.shutup.circle.common.Constants;
import com.shutup.circle.common.GsonSingleton;
import com.shutup.circle.common.RetrofitSingleton;
import com.shutup.circle.common.SystemBarTintManager;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by shutup on 2016/12/14.
 */

public class BaseActivity extends AppCompatActivity implements Constants{

    private CircleApi mCircleApi;
    private Gson mGson;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initStatusBar();
        initApiInstance();
        initGsonInstance();
        super.onCreate(savedInstanceState);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
    }

    @TargetApi(19)
    public void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public void setStatusBarColor(@ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(false);
        tintManager.setStatusBarTintResource(color);//通知栏所需颜色
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

    protected void refreshUI() {
        Message m = new Message();
        m.what = 1;
        EventBus.getDefault().postSticky(m);
    }
}
