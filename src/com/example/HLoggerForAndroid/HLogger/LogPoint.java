package com.example.HLoggerForAndroid.HLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;

/**
 * Created by liubo on 14-9-16.
 */
public class LogPoint
{
	/**
	 * 获取类的名称
	 * */
	public static String getClassName(Context context)
	{
		String contextString = context.toString();
		return contextString.substring(contextString.lastIndexOf(".") + 1,
				contextString.indexOf("@"));
	}

	/**
	 * 获取应用程序的版本号
	 * */
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

	/**
	 * 获取系统的ID——IMEI
	 * */
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

	/**
	 * 获取设备的类型
	 * */
	public static String getDeviceType()
	{
		String deviceType = android.os.Build.MODEL;
		return deviceType;
	}

	/**
	 * 获取系统的版本号
	 * */
	public static String getSysVersion()
	{
		String systemVersion = "" + android.os.Build.VERSION.RELEASE;
		return systemVersion;
	}

	/**
	 * 获取当前的时间，本质是long型
	 * */
	public static String getCurDate()
	{

		// SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		// String str = formatter.format(curDate);
		// Log.i("date", "" + str);
		String str = "" + System.currentTimeMillis();
		return str;
	}

	/**
	 * 获取网络类型
	 * */
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

		}

		return network;

	}

	/**
	 * ip地址
	 * */
	public static String getIpAddress(Context context)
	{

		String network = "";
		String ipAddress = null;
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			NetworkInfo ni = cm.getActiveNetworkInfo();
			if (ni == null) {
				// Toast.makeText(getApplicationContext(), "当前无网络连接", Toast.LENGTH_SHORT).show();

				return "null";
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
		return ipAddress;
	}

	/**
	 * 转行ip地址
	 * */
	public static String intToIp(int i)
	{

		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "."
				+ (i >> 24 & 0xFF);
	}

	/**
	 * 获取mobile状态下的ip地址
	 * */
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

	/**
	 * 获取AppId
	 * */
	public static String getAppId(Context context)
	{
		PackageInfo info;
		String packageNames = null;
		try {
			info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			// 当前版本的包名
			packageNames = info.packageName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return packageNames;
	}

	// 获得 请求方式，信息类型（常量）
	/**
	 * POST网络请求方式
	 * */
	public static final String POST = "LogPost";
	/**
	 * GET网络请求方式
	 * */
	public static final String GET = "LogGet";
	/**
	 * Error类型信息
	 * */
	public static final String ERROR = "LogError";
	/**
	 * Warning类型信息
	 * */
	public static final String WARNING = "LogWarning";
	/**
	 * Info类型信息
	 * */
	public static final String INFO = "LogInfo";

	// 预留 系统状态（CPU，内存）
	/**
	 * 获取系统的CPU使用率
	 * */
	public static String testCpuRate()
	{
		// -m 10, how many entries you want, -d 1, delay by how much, -n 1,
		// number of iterations
		// top m 15 -d 1 -n 1
		String Result;
		StringBuilder tv = null;
		Process p = null;
		try {
			p = Runtime.getRuntime().exec("top -n 1");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		try {
			while ((Result = br.readLine()) != null) {
				if (Result.trim().length() < 1) {
					continue;
				} else {
					tv = new StringBuilder();
					String[] CPUusr = Result.split("%");
					tv.append("USER:" + CPUusr[0] + "\n");
					String[] CPUusage = CPUusr[0].split("User");
					String[] SYSusage = CPUusr[1].split("System");
					tv.append("CPU:" + CPUusage[1].trim() + " length:"
							+ CPUusage[1].trim().length() + "\n");
					tv.append("SYS:" + SYSusage[1].trim() + " length:"
							+ SYSusage[1].trim().length() + "\n");
					tv.append(Result + "\n");
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tv.toString();
	}

	/**
	 * 获取系统可用的内存信息
	 * */
	public static String getSysAvaialbeMemorySize(Context context)
	{
		ActivityManager mActivityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		// 获得MemoryInfo对象
		MemoryInfo memoryInfo = new MemoryInfo();
		// 获得系统可用内存，保存在MemoryInfo对象上
		mActivityManager.getMemoryInfo(memoryInfo);
		long memSize = memoryInfo.availMem;

		// 字符类型转换
		String availMemStr = formateFileSize(context, memSize);

		return availMemStr;
	}

	/**
	 * 调用系统函数，字符串转换 long -String KB/MB
	 * */
	private static String formateFileSize(Context context, long size)
	{
		return Formatter.formatFileSize(context, size);
	}

	/**
	 * 获取系统电量
	 * */
	public static String getSysBat()
	{
		return null;
	}

}
