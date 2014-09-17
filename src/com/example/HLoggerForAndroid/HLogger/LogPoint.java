package com.example.HLoggerForAndroid.HLogger;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by liubo on 14-9-16.
 */
public class LogPoint
{
	public static String ipAddress;


	public static String getClassyName(Context context)
	{
		String contextString = context.toString();
		return contextString.substring(contextString.lastIndexOf(".") + 1,
				contextString.indexOf("@"));
	}

	public static String getAppVersion(Context context)// 获取版本号
	{
		try {
			PackageInfo pi = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return pi.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "unknown";
		}
	}

	static TelephonyManager telephonyManager;

	public static String getSysImei(Context context)
	{
		// TODO Auto-generated method stub
		telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();
		Log.i("imei", "" + imei);
		if (null == imei) {
			imei = "";
		}
		return imei;
	}

	public static String getDeviceType()
	{
		String deviceType = android.os.Build.MODEL;
		return deviceType;
	}

	public static String getSysVersion()
	{
		String systemVersion = "" + android.os.Build.VERSION.RELEASE;
		return systemVersion;
	}


    //时间戳
	public static String getCurDate()
	{

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		Log.i("date", "" + str);
		return str;
	}

	public static String getNetType(Context context)
	{

		String network = "";
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			NetworkInfo ni = cm.getActiveNetworkInfo();
			if (ni == null) {
				// Toast.makeText(getApplicationContext(), "当前无网络连接", Toast.LENGTH_SHORT).show();

				return "net work not avaiable";
			}

			network = ni.getTypeName();

			if (ni.getTypeName().equals("WIFI")) {
				WifiManager wifiManager = (WifiManager) context
						.getSystemService(Context.WIFI_SERVICE);
				WifiInfo wifiInfo = wifiManager.getConnectionInfo();
				int ip = wifiInfo.getIpAddress();
				ipAddress = intToIp(ip);
			} else if (ni.getType() == ConnectivityManager.TYPE_MOBILE) {
				// 判断3G网
				ipAddress = getLocalIpAddress();
			}

		}

		return network;

	}

	public static String intToIp(int i)
	{

		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "."
				+ (i >> 24 & 0xFF);
	}

	public static String getLocalIpAddress()
	{
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
					.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
						.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("WifiPreference IpAddress", ex.toString());
		}
		return null;
	}

    //获得appID

    //获得 请求方式，信息类型（常量）

    //预留 系统状态（CPU，内存）

}
