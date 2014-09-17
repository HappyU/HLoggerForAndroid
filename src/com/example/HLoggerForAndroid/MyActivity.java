package com.example.HLoggerForAndroid;

import android.app.Activity;
import android.os.Bundle;

import com.example.HLoggerForAndroid.HLogger.LogPoint;

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
		speString = LogPoint.getIpAddress(this);
		speString = LogPoint.getAppId(this);
		speString = LogPoint.getSysAvaialbeMemorySize(this);
		speString = LogPoint.testCpuRate();

	}
}
