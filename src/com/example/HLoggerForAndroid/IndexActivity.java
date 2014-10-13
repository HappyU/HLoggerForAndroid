package com.example.HLoggerForAndroid;

import android.app.Activity;
import android.os.Bundle;
import com.example.HLoggerForAndroid.HLogger.Logger;

/**
 * Created by liubo on 14-10-11.
 */
public class IndexActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Logger.operate("测试二 IndexActivity");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Logger.operateStartDate = Logger.getCurDate();
    }

}