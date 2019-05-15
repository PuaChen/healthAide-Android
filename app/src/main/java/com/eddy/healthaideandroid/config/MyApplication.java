package com.eddy.healthaideandroid.config;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;


public class MyApplication extends Application {

    private static Context CONTEXT;

    @Override
    public void onCreate() {
        super.onCreate();
        CONTEXT = getApplicationContext();
        QMUISwipeBackActivityManager.init(this);
    }

    public static Context getContext() {
        return CONTEXT;
    }

    @Override
    public Resources.Theme getTheme() {
        return super.getTheme();
    }
}
