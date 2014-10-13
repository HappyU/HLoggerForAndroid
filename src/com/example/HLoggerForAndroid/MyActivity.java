package com.example.HLoggerForAndroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.example.HLoggerForAndroid.HLogger.LogFile;
import com.example.HLoggerForAndroid.HLogger.Logger;

public class MyActivity extends Activity
{
	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);


        Button operateBtn = (Button)findViewById(R.id.btn);
        operateBtn.setTag("operate");
        operateBtn.setOnClickListener(new MyClickListener());

        Button infoBtn = (Button)findViewById(R.id.infoBtn);
        infoBtn.setTag("info");
        infoBtn.setOnClickListener(new MyClickListener());

        Button warnBtn = (Button)findViewById(R.id.warnBtn);
        warnBtn.setTag("warn");
        warnBtn.setOnClickListener(new MyClickListener());

        Button debugBtn = (Button)findViewById(R.id.debugBtn);
        debugBtn.setTag("debug");
        debugBtn.setOnClickListener(new MyClickListener());

        Button errorBtn = (Button)findViewById(R.id.errorBtn);
        errorBtn.setTag("error");
        errorBtn.setOnClickListener(new MyClickListener());

        Button submitBtn = (Button)findViewById(R.id.submitBtn);
        submitBtn.setTag("submit");
        submitBtn.setOnClickListener(new MyClickListener());



	}


    class MyClickListener implements View.OnClickListener
    {

        @Override
        public void onClick(View view)
        {
            if (view.getTag().equals("info"))
            {
                Logger.info("点击 info 按钮！");
            }
            else if(view.getTag().equals("warn"))
            {
                Logger.warn("点击 warn 按钮！");
            }
            else if(view.getTag().equals("operate"))
            {
                Intent intent = new Intent(MyActivity.this,IndexActivity.class);
                startActivity(intent);
            }
            else if(view.getTag().equals("debug"))
            {
                Logger.debug("点击 debug 按钮");
            }
            else if(view.getTag().equals("error"))
            {
                String str= null;
                boolean b = str.equals("d")?true:false;
            }
            else if (view.getTag().equals("submit"))
            {
                LogFile.getInstance("MyActivity").uploadInfo();
            }
        }
    }


    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Logger.operate("测试一 MyActivity");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Logger.operateStartDate = Logger.getCurDate();
    }
}
