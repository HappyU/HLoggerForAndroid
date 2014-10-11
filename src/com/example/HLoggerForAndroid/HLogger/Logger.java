package com.example.HLoggerForAndroid.HLogger;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.telephony.TelephonyManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liubo on 14-9-25.
 */
public class Logger
{

   /*-------------关键点信息--begin-----------------*/

    private static Context context = ContextUtil.getInstance();

    /**
     * 获取系统的ID——IMEI
     * */
    private static String getDeviceId()
    {
        // TODO Auto-generated method stub
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        if (null == imei) {
            imei = "";
        }

        return imei;
    }


    /**
     * 获取设备的类型
     * */
    private static String getDeviceType()
    {
        String deviceType = android.os.Build.MODEL;
        return deviceType;
    }

    /**
     * 获取系统的版本号
     * */
    private static String getSysVersion()
    {
        String systemVersion = "" + android.os.Build.VERSION.RELEASE;
        return systemVersion;
    }

    /**
     * 获取AppId
     * */
    private static String getAppId()
    {
        PackageInfo info;
        String packageNames = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            // 当前版本的包名
            packageNames = info.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageNames;
    }

    /**
     * 获取应用程序的版本号
     * */
    private static String getAppVersion()// 获取版本号
    {
        try {
            PackageInfo pi = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "unknown";
        }
    }


    /**
     * 获取当前的时间，本质是long型
     * */
    private static String getCurDate()
    {
        String str = "" + System.currentTimeMillis()/1000;
        return str;
    }

    /*-------------关键点信息--end-----------------*/


    private static final String TAG = "";

    // 日志类型标识符(优先级：由低到高排列，取值越小优先级越高)
    public static final int SHOW_ERROR_LOG   = 1;
    public static final int SHOW_WARN_LOG    = 2;
    public static final int SHOW_INFO_LOG    = 3;
    public static final int SHOW_OPERATE_LOG = 4;
    public static final int SHOW_DEBUG_LOG   = 5;


    public static final int LEVEL_INFO = SHOW_OPERATE_LOG;


    private static LogFile logFile = LogFile.getInstance(TAG);


    public static void error( String msg )
    {
        boolean isShow = LEVEL_INFO >= SHOW_ERROR_LOG?true:false;
        if (!isShow)
        {
            return;
        }
        if  (null == msg || msg.trim().equals(""))
        {
            return;
        }

        RecordBean.type = "error";
        RecordBean.eventID = "异常";
        RecordBean.eventClass = "";
        RecordBean.startDate = getCurDate();
        RecordBean.endDate = "";
        RecordBean.content = msg;
        RecordBean.userID = "";
        RecordBean.netState = "";
        String strMsg = getOutString();
        android.util.Log.i(TAG,strMsg);

        logFile.readyWrite(strMsg,new LogFile.ILog()
        {
            @Override
            public String getHeaderInfo()
            {
                return getHeaderAndFirstInfo();
            }
        });

    }

    public static void warn( String msg )
    {
        boolean isShow = LEVEL_INFO >= SHOW_WARN_LOG?true:false;
        if (!isShow)
        {
            return;
        }
        if  (null == msg || msg.trim().equals(""))
        {
            return;
        }


        String cName = new Throwable().getStackTrace()[1].getClassName();
        String mName = new Throwable().getStackTrace()[1].getMethodName();

        RecordBean.type = "warn";
        RecordBean.eventID = "警告";
        RecordBean.eventClass = "class："+cName+"__method："+mName;
        RecordBean.startDate = "";
        RecordBean.endDate = "";
        RecordBean.content = msg;
        RecordBean.userID = "";
        RecordBean.netState = "";
        String strMsg = getOutString();
        android.util.Log.i(TAG,strMsg);
        logFile.writeLogFile(strMsg,new LogFile.ILog()
        {
            @Override
            public String getHeaderInfo()
            {
                return getHeaderAndFirstInfo();
            }
        });
    }

    public static void info( String msg )
    {
        boolean isShow = LEVEL_INFO >= SHOW_INFO_LOG?true:false;
        if (!isShow)
        {
            return;
        }
        if  (null == msg || msg.trim().equals(""))
        {
            return;
        }

        String cName = new Throwable().getStackTrace()[1].getClassName();
        String mName = new Throwable().getStackTrace()[1].getMethodName();

        RecordBean.type = "info";
        RecordBean.eventID = "信息";
        RecordBean.eventClass = "class："+cName+"__method："+mName;
        RecordBean.startDate = "";
        RecordBean.endDate = "";
        RecordBean.content = msg;
        RecordBean.userID = "";
        RecordBean.netState = "";
        String strMsg = getOutString();

        android.util.Log.i(TAG,strMsg);
        logFile.writeLogFile(strMsg,new LogFile.ILog()
        {
            @Override
            public String getHeaderInfo()
            {
                return  getHeaderAndFirstInfo();
            }
        });
    }

    public static void operate( String msg )
    {
        boolean isShow = LEVEL_INFO >= SHOW_OPERATE_LOG?true:false;
        if (!isShow)
        {
            return;
        }
        if  (null == msg || msg.trim().equals(""))
        {
            return;
        }


        RecordBean.type = "operate";
        RecordBean.eventID = msg;
        RecordBean.eventClass = "";
        RecordBean.startDate = getCurDate();
        RecordBean.endDate = getCurDate();
        RecordBean.content = "";
        RecordBean.userID = "";
        RecordBean.netState = "";
        String strMsg = getOutString();

        android.util.Log.i(TAG,strMsg);
        logFile.writeLogFile(strMsg,new LogFile.ILog()
        {
            @Override
            public String getHeaderInfo()
            {
                return getHeaderAndFirstInfo();
            }
        });
    }

    public static void debug( String msg )
    {
        boolean isShow = LEVEL_INFO >= SHOW_DEBUG_LOG?true:false;
        if (!isShow)
        {
            return;
        }
        if  (null == msg || msg.trim().equals(""))
        {
            return;
        }

        String cName = new Throwable().getStackTrace()[1].getClassName();
        String mName = new Throwable().getStackTrace()[1].getMethodName();

        RecordBean.type = "info";
        RecordBean.eventID = "信息";
        RecordBean.eventClass = "class："+cName+"__method："+mName;
        RecordBean.startDate = "";
        RecordBean.endDate = "";
        RecordBean.content = msg;
        RecordBean.userID = "";
        RecordBean.netState = "";
        String strMsg = getOutString();

        android.util.Log.i(TAG,strMsg);
        logFile.writeLogFile(strMsg,new LogFile.ILog()
        {
            @Override
            public String getHeaderInfo()
            {
                return getHeaderAndFirstInfo();
            }
        });
    }

    public static String getHeaderAndFirstInfo()
    {

        String headerStr = "type,eventID,eventClass,content,startDate,endDate,userID,netState\n";

        String firstStr = "header#" +
                getDeviceId()+"#" +
                getDeviceType()+"#" +
                getSysVersion()+"#" +
                getAppId()+"#" +
                getAppVersion();

        RecordBean.type = "header";
        RecordBean.eventID = "";
        RecordBean.eventClass = "";
        RecordBean.startDate = "";
        RecordBean.endDate = "";
        RecordBean.content = firstStr;
        RecordBean.userID = "";
        RecordBean.netState = "";
        String strMsg = getOutString();



        return headerStr+strMsg;
    }



    private static String getOutString()
    {
        String str = "\""+RecordBean.type+"\",\""+RecordBean.eventID+"\",\""+RecordBean.eventClass+"\",\""+RecordBean.content+"\",\""+RecordBean.startDate+"\",\""+RecordBean.endDate+"\",\""+RecordBean.userID+"\",\""+RecordBean.netState+"\"\n";
        return str;
    }



    static private class RecordBean
    {
        static private String type;
        static private String eventID;
        static private String eventClass;
        static private String content;
        static private String startDate;
        static private String endDate;
        static private String userID;
        static private String netState;



    }

}


