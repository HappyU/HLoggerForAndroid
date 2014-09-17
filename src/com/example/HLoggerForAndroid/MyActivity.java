package com.example.HLoggerForAndroid;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.example.HLoggerForAndroid.HLogger.HLog;

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
		String speString;
		// speString = LogPoint.getDate();
		// speString = LogPoint.getDeviceType();
		// speString = LogPoint.getImei(getApplicationContext());
		// speString = LogPoint.getNetType(getApplicationContext());
		// speString = LogPoint.getRunningActivityName(this);
		// speString = LogPoint.getSystemVersion();
		// speString = LogPoint.getVersion(getApplicationContext());
		// speString = LogPoint.getIpAddress(this);
		// speString = LogPoint.getAppId(this);
		// speString = LogPoint.getSysAvaialbeMemorySize(this);
		// speString = LogPoint.testCpuRate();
		HLog hLog = new HLog(this);
		speString = hLog.getSt();
		hLog.getClassInfo();
		hLog.getAssetPro();
		java.util.Map<Thread, StackTraceElement[]> ts = Thread.getAllStackTraces();// .getAllStackTraces()
		StackTraceElement[] ste = ts.get(Thread.currentThread());
		for (StackTraceElement s : ste) {
			Log.i("TestApp", s.toString()); // 这个是android自带的，如果没有，用其他的打印函数一样
		}

	}
}
