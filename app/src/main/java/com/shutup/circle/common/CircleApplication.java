package com.shutup.circle.common;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by shutup on 2017/4/4.
 */

public class CircleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
        Realm.init(getApplicationContext());
    }
}
