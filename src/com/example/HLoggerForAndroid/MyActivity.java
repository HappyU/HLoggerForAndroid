package com.example.HLoggerForAndroid;

import android.app.Activity;
import android.os.Bundle;

import android.util.Log;
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

        Logger.info("nihaodsdsdsdsd");

	}
}
