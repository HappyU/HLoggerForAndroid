package com.example.HLoggerForAndroid.HLogger;

import android.app.Application;

/**
 * Created by liubo on 14-9-25.
 */
public class ContextUtil extends Application
{
    private static ContextUtil instance;

    public static ContextUtil getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        CrashHandler catchHandler = CrashHandler.getInstance();
        catchHandler.init(getApplicationContext());
    }
}
