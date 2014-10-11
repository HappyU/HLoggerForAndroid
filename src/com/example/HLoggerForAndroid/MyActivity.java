package com.example.HLoggerForAndroid;

import android.app.Activity;
import android.os.Bundle;

import android.util.Log;
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

//        Logger.info("大家好，cxcxcxcxcxcxcxcxc");
//        Logger.warn("warn info1 ,hello everybody,this is warn info-1111111");
//        Logger.warn("warn info2 ,hello everybody,this is warn info-2222222");
//        Logger.warn("warn info3 ,hello everybody,this is warn info-3333333");
//        Logger.warn("warn info4 ,hello everybody,this is warn info-4444444");
//        Logger.warn("warn info5 ,hello everybody,this is warn info-5555555");

        LogFile.getInstance("TAG").uploadInfo();


//        String str = null;
//        if(str.equals("dd"))
//        {
//            Logger.info("相同");
//        }
//        else
//        {
//            Logger.info("不相同");
//        }

	}
}
